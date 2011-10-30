package org.onehippo.forge.stripes.integration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.controller.DispatcherServlet;

/**
 * @version $Id$
 */
public class StripesDispatchWrapper extends DispatcherServlet {
    private static final long serialVersionUID = 1L;


    public void invokeDoGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }

}
