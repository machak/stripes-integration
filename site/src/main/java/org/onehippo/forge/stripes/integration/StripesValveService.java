package org.onehippo.forge.stripes.integration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.request.HstRequestContext;

/**
 * @version $Id$
 */
public interface StripesValveService {

    void invoke(HstRequestContext requestContext, HttpServletRequest request, HttpServletResponse response) throws ContainerException;
}
