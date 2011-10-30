
package org.onehippo.forge.stripes.beans;

import java.util.Calendar;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;

@Node(jcrType="stripesintegration:newsdocument")
public class NewsDocument extends TextDocument{

    public Calendar getDate() {
        return getProperty("stripesintegration:date");
    }

    /**
     * Get the imageset of the newspage
     *
     * @return the imageset of the newspage
     */
    public HippoGalleryImageSetBean getImage() {
        return getLinkedBean("stripesintegration:image", HippoGalleryImageSetBean.class);
    }
    
}
