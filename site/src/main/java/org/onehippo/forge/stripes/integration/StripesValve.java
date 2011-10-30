package org.onehippo.forge.stripes.integration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hippoecm.hst.core.container.AbstractValve;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version $Id$
 */
public class StripesValve extends AbstractValve {


    private final StripesService service;

    public StripesValve(final StripesService stripesService) {
        service = stripesService;
    }


    @Override
    public void invoke(final ValveContext context) throws ContainerException {

        HttpServletRequest servletRequest = context.getServletRequest();
        HttpServletResponse servletResponse = context.getServletResponse();
        HstRequestContext requestContext = context.getRequestContext();
        service.invoke(requestContext, servletRequest, servletResponse);

    }
}
