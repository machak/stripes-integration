package org.onehippo.forge.stripes.integration.ctx;

import javax.servlet.http.HttpServletRequest;

import org.hippoecm.hst.container.HstContainerRequest;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.forge.stripes.integration.Const;
import org.onehippo.forge.stripes.integration.StripesHttpServletRequestWrapper;

import net.sourceforge.stripes.action.ActionBeanContext;

/**
 * @version $Id$
 */
public class HstBeanContext extends ActionBeanContext {



    public HippoBean getContentBean(){
        return  getStripesRequest().getContent();
    }

    public StripesHttpServletRequestWrapper getStripesRequest() {
        return (StripesHttpServletRequestWrapper) getRequest().getAttribute(Const.STRIPES_HST_REQUEST);
    }
}
