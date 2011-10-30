
package org.onehippo.forge.stripes.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType="stripesintegration:textdocument")
public class TextDocument extends BaseDocument{

    public HippoHtml getHtml(){
        return getHippoHtml("stripesintegration:body");    
    }
}
