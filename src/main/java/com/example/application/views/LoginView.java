package com.example.application.views;

import com.example.application.views.list.ListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route("login")
@PageTitle("Login | Searchminar")
public class LoginView extends VerticalLayout implements BeforeEnterListener {

    private LoginForm login = new LoginForm();

    public LoginView() {
        login.addLoginListener(evt -> {
            UI.getCurrent().navigate("");
        });
        Image logo = new Image("/images/logo_searchminar.png", "Logo");
        H1 text = new H1("Searchminar");
        text.addClassName("login-text");
        logo.addClassName("login-logo");
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);
        RouterLink registerView = new RouterLink("Not registered ? Register here", RegisterView.class);
        add(
                logo,
                text,
                login,
                registerView
        );

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")){
            login.setError(true);
        }
    }
}
