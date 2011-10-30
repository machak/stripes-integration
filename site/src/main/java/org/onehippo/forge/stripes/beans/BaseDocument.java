
package org.onehippo.forge.stripes.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType="stripesintegration:basedocument")
public class BaseDocument extends HippoDocument {

    public String getTitle() {
        return getProperty("stripesintegration:title");
    }
    
    public String getSummary() {
        return getProperty("stripesintegration:summary");
    }
}
