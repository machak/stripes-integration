package org.onehippo.forge.stripes.integration;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.GenericHttpServletRequestWrapper;
import org.jdom.Content;

/**
* @version $Id$
*/
public class StripesHttpServletRequestWrapper extends GenericHttpServletRequestWrapper {

    private String requestURI;
    private String requestURL;
    private HstRequestContext requestContext;
    private String matrixParamsSuffix;
    private HippoBean content;

    public StripesHttpServletRequestWrapper(HstRequestContext requestContext, HttpServletRequest request, String servletPath, String requestPath, final HippoBean node) {
        super(request);
        this.content = node;
        setServletPath(servletPath);

        String tempPathInfo = null;
        String tempMatrixParamsSuffix = null;

        if (requestPath != null) {
            int offset = requestPath.indexOf(';');
            if (offset == -1) {
                tempPathInfo = requestPath;
            } else {
                tempPathInfo = requestPath.substring(0, offset);
                tempMatrixParamsSuffix = requestPath.substring(offset);
            }
        }

        setPathInfo(tempPathInfo);
        this.matrixParamsSuffix = tempMatrixParamsSuffix;

        this.requestContext = requestContext;
    }

    @Override
    public String getRequestURI() {
        if (requestURI == null) {
            StringBuilder sbTemp = new StringBuilder(getContextPath()).append(getServletPath());
            String pathInfo = getPathInfo();
            if (pathInfo != null) {
                sbTemp.append(pathInfo);
            }
            if (matrixParamsSuffix != null) {
                sbTemp.append(matrixParamsSuffix);
            }
            requestURI = sbTemp.toString();

            if (requestURI.length() == 0) {
                requestURI = "/";
            }
        }

        return requestURI;
    }

    public HippoBean getContent() {
        return content;
    }
}
