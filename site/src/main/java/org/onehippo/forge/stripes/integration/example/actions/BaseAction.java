package org.onehippo.forge.stripes.integration.example.actions;

import org.onehippo.forge.stripes.integration.example.ExampleContext;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

/**
 * @version $Id$
 */
public abstract class BaseAction implements ActionBean {

    private ExampleContext context;

    @Override
    public void setContext(final ActionBeanContext context) {
        this.context = (ExampleContext) context;
    }

    @Override
    public ExampleContext getContext() {
        return context;
    }
}
