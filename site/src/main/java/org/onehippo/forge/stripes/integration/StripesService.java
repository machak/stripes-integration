package org.onehippo.forge.stripes.integration;

import java.io.IOException;
import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.container.HstContainerRequestImpl;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ContainerNotFoundException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedMount;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.jaxrs.util.AnnotatedContentBeanClassesScanner;
import org.hippoecm.hst.util.ObjectConverterUtils;
import org.hippoecm.hst.util.PathUtils;
import org.hippoecm.repository.api.HippoNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.stripes.controller.StripesRequestWrapper;

/**
 * @version $Id$
 */
public class StripesService implements StripesValveService {

    private static Logger log = LoggerFactory.getLogger(StripesService.class);


    public static final String BEANS_ANNOTATED_CLASSES_CONF_PARAM = "hst-beans-annotated-classes";

    private String annotatedClassesResourcePath;
    private List<Class<? extends HippoBean>> annotatedClasses;
    private ObjectConverter objectConverter;


    private String servletPath;
    private String serviceName;

    public StripesService(final String serviceName) {
        this.serviceName = serviceName;
    }


    @Override
    public void invoke(final HstRequestContext requestContext, HttpServletRequest httpRequest, final HttpServletResponse response) throws ContainerException {
        log.info("*** invoking stripes service *** ");
        try {
            HstContainerRequestImpl r = (HstContainerRequestImpl) httpRequest;
            StripesRequestWrapper stripesWrapper = StripesRequestWrapper.findStripesWrapper(r.getRequest());
            stripesWrapper.setAttribute(Const.STRIPES_HST_REQUEST, getStripesRequest(requestContext, httpRequest));
            stripesWrapper.setAttribute(Const.STRIPES_HST_REQUEST_CONTEXT, requestContext);
            RequestDispatcher dispatcher = requestContext.getServletContext().getNamedDispatcher("StripesDispatcher");
            dispatcher.include(stripesWrapper, response);
        } catch (ServletException e) {

        } catch (IOException e) {

        }


    }


    protected String getMountContentPath(HstRequestContext requestContext) {
        return requestContext.getResolvedMount().getMount().getContentPath();
    }


    protected HttpServletRequest getStripesRequest(HstRequestContext requestContext, HttpServletRequest request) throws ContainerException {
        String contentPathInfo;

        ResolvedSiteMapItem resolvedSiteMapItem = requestContext.getResolvedSiteMapItem();
        if (resolvedSiteMapItem == null) {
            contentPathInfo = PathUtils.normalizePath(requestContext.getBaseURL().getPathInfo());
        } else {
            contentPathInfo = resolvedSiteMapItem.getRelativeContentPath();
        }

        String requestContentPath = getMountContentPath(requestContext) + "/hst:content/"+ (contentPathInfo != null ? contentPathInfo : "");

        Node node;
        String resourceType;

        try {
            Session jcrSession = requestContext.getSession();
            // TODO change:
            requestContentPath = requestContentPath.replaceAll("\\.action","");


            node = getContentNode(jcrSession, requestContentPath);
            if (node == null) {
                throw new ContainerNotFoundException("Cannot find content node at '" + requestContentPath + '\'', new WebApplicationException(Response.Status.NOT_FOUND));
            }
            resourceType = getObjectConverter(requestContext).getPrimaryObjectType(node);
            if (resourceType == null) {
                throw new ContainerException("Cannot find the resourceType for node '" + node.getPath() + "' with primary type '" + node.getPrimaryNodeType().getName() + "'", new WebApplicationException(Response.Status.NOT_FOUND));
            }
        } catch (PathNotFoundException pnf) {
            throw new ContainerNotFoundException(new WebApplicationException(Response.Status.NOT_FOUND));
        } catch (LoginException e) {
            throw new ContainerException(e);
        } catch (RepositoryException e) {
            throw new ContainerException(e);
        } catch (ObjectBeanManagerException e) {
            throw new ContainerException(e);
        }

        requestContext.setAttribute(Const.REQUEST_CONTENT_PATH_KEY, requestContentPath);
        requestContext.setAttribute(Const.REQUEST_CONTENT_NODE_KEY, node);


        return new StripesHttpServletRequestWrapper(requestContext, request, getStripesServletPath(requestContext), requestContentPath, convert(node));
    }


    protected String getStripesServletPath(HstRequestContext requestContext) throws ContainerException {
        ResolvedMount resolvedMount = requestContext.getResolvedMount();
        return new StringBuilder(resolvedMount.getResolvedMountPath()).append(getServletPath()).toString();
    }

    protected Node getContentNode(Session session, String path) throws RepositoryException {
        if (path == null || !path.startsWith("/")) {
            log.warn("Illegal argument for '{}' : not an absolute path", path);
            return null;
        }
        String relPath = path.substring(1);
        Node node = session.getRootNode();
        String nodePath = node.getPath();
        if (!node.hasNode(relPath)) {
            log.info("Cannot get object for node '{}' with relPath '{}'", nodePath, relPath);
            return null;
        }
        Node relNode = node.getNode(relPath);
        if (relNode.isNodeType(HippoNodeType.NT_HANDLE)) {
            // if its a handle, we want the child node. If the child node is not present,
            // this node can be ignored
            if (relNode.hasNode(relNode.getName())) {
                return relNode.getNode(relNode.getName());
            } else {
                log.info("Cannot get object for node '{}' with relPath '{}'", nodePath, relPath);
                return null;
            }
        } else {
            return relNode;
        }
    }

    private HippoBean convert(final Node relNode) {
        try {
           return (HippoBean) objectConverter.getObject(relNode);
        } catch (ObjectBeanManagerException e) {
            log.error("Error converting node", e);
        }
        return null;
    }


    protected ObjectConverter getObjectConverter(HstRequestContext requestContext) {
        if (objectConverter == null) {
            List<Class<? extends HippoBean>> annotatedClasses = getAnnotatedClasses(requestContext);
            objectConverter = ObjectConverterUtils.createObjectConverter(annotatedClasses);
        }
        return objectConverter;
    }


    public String getAnnotatedClassesResourcePath() {
        return annotatedClassesResourcePath;
    }


    public void setAnnotatedClassesResourcePath(String annotatedClassesResourcePath) {
        this.annotatedClassesResourcePath = annotatedClassesResourcePath;
    }

    public void setAnnotatedClasses(List<Class<? extends HippoBean>> annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
    }

    public void setObjectConverter(ObjectConverter objectConverter) {
        this.objectConverter = objectConverter;
    }

    protected List<Class<? extends HippoBean>> getAnnotatedClasses(HstRequestContext requestContext) {
        if (annotatedClasses == null) {
            String annoClassPathResourcePath = getAnnotatedClassesResourcePath();

            if (StringUtils.isBlank(annoClassPathResourcePath)) {
                annoClassPathResourcePath = requestContext.getServletContext().getInitParameter(BEANS_ANNOTATED_CLASSES_CONF_PARAM);
            }

            annotatedClasses = AnnotatedContentBeanClassesScanner.scanAnnotatedContentBeanClasses(requestContext, annoClassPathResourcePath);
        }

        return annotatedClasses;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public String getServletPath() {
        return servletPath;
    }

    public String getServiceName() {
        return serviceName;
    }

}
