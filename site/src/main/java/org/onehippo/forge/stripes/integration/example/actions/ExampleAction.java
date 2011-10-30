package org.onehippo.forge.stripes.integration.example.actions;

import org.onehippo.forge.stripes.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

/**
 * @version $Id$
 */

@UrlBinding("/web/{view}.action")
public class ExampleAction extends BaseAction {


    private String view;

    @ValidateNestedProperties({
            @Validate(field = "name", required = true, label = "User name")
    })
    private User user;

    @DontValidate
    @DefaultHandler
    public Resolution index() {
        return new ForwardResolution("/web/example/index.jsp");
    }


    public Resolution saveUser() {
        // TODO save user into database
        getContext().getRequest().setAttribute("user", user);
        return new ForwardResolution("/web/example/index.jsp");
    }

    @Before(stages = LifecycleStage.BindingAndValidation)
    public void loadDocument() {
        getContext().getRequest().setAttribute("document", getContext().getContentBean());
    }


    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getView() {
        return view;
    }

    public void setView(final String view) {
        this.view = view;
    }
}
