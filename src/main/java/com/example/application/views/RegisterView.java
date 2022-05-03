package com.example.application.views;


import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.security.PermitAll;

@Route("register")
@PageTitle("Register | Searchminar")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {
    private PasswordField password;
    private PasswordField confirmPassword;

    private UserService service;
    private BeanValidationBinder<User> binder;

    /**
     * Flag for disabling first run for password validation
     */
    private boolean enablePasswordValidation;

    /**
     * We use Spring to inject the backend into our view
     */
    public RegisterView(UserService service) {

        this.service = service;

        /*
         * Create the components we'll need
         */
        Image logo = new Image("/images/logo_searchminar.png", "Logo");
        H1 text = new H1("Searchminar");
        text.addClassName("login-text");
        logo.addClassName("login-logo");

        H3 title = new H3("Register");

        TextField username = new TextField("Username");

        EmailField email = new EmailField("Email");

        password = new PasswordField("Password");
        confirmPassword = new PasswordField("Confirm Password");

        Span errorMessage = new Span();

        Button submitButton = new Button("Register");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        /*
         * Build the visible layout
         */

        // Create a FormLayout with all our components. The FormLayout doesn't have any
        // logic (validation, etc.), but it allows us to configure Responsiveness from
        // Java code and its defaults looks nicer than just using a VerticalLayout.
        RouterLink loginView = new RouterLink("Already registered ? Login here", LoginView.class);
        FormLayout formLayout = new FormLayout(title, username, email, password, confirmPassword, errorMessage, submitButton);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Restrict maximum width and center on page
        formLayout.setMaxWidth("500px");
        formLayout.getStyle().set("margin", "0 auto");

        // Allow the form layout to be responsive. On device widths 0-490px we have one
        // column, then we have two. Field labels are always on top of the fields.
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        // These components take full width regardless if we use one column or two (it
        // just looks better that way)
        formLayout.setColspan(title, 2);
        formLayout.setColspan(errorMessage, 2);
        formLayout.setColspan(submitButton, 2);

        //setAlignItems(Alignment.CENTER);
        // Add some styles to the error message to make it pop out
        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)");
        errorMessage.getStyle().set("padding", "15px 0");

        // Add the form to the page

        add(logo, text, formLayout, loginView);

        /*
         * Set up form functionality
         */

        /*
         * Binder is a form utility class provided by Vaadin. Here, we use a specialized
         * version to gain access to automatic Bean Validation (JSR-303). We provide our
         * data class so that the Binder can read the validation definitions on that
         * class and create appropriate validators. The BeanValidationBinder can
         * automatically validate all JSR-303 definitions, meaning we can concentrate on
         * custom things such as the passwords in this class.
         */
        binder = new BeanValidationBinder<User>(User.class);

        // Basic name fields that are required to fill in
        binder.forField(username).asRequired().withValidator(this::usernameValidator).bind("username");

        // EmailField uses a Validator that extends one of the built-in ones.
        // Note that we use 'asRequired(Validator)' instead of
        // 'withValidator(Validator)'; this method allows 'asRequired' to
        // be conditional instead of always on. We don't want to require the email if
        // the user declines marketing messages.
        binder.forField(email).asRequired().withValidator(this::emailValidator).bind("email");

        // Another custom validator, this time for passwords
        binder.forField(password).asRequired().withValidator(this::passwordValidator).bind("password");
        // We won't bind passwordField2 to the Binder, because it will have the same
        // value as the first field when correctly filled in. We just use it for
        // validation.

        // The second field is not connected to the Binder, but we want the binder to
        // re-check the password validator when the field value changes. The easiest way
        // is just to do that manually.
        confirmPassword.addValueChangeListener(e -> {

            // The user has modified the second field, now we can validate and show errors.
            // See passwordValidator() for how this flag is used.
            enablePasswordValidation = true;

            binder.validate();
        });

        // A label where bean-level error messages go
        binder.setStatusLabel(errorMessage);

        // And finally the submit button
        submitButton.addClickListener(e -> {
            try {

                // Create empty bean to store the details into
                User detailsBean = new User();
                detailsBean.setRole("USER");
                // Run validators and write the values to the bean

                binder.writeBean(detailsBean);

                // Call backend to store the data
                service.store(detailsBean);

                // Show success message if everything went well
                showSuccess(detailsBean);

            } catch (ValidationException e1) {
                // validation errors are already visible for each field,
                // and bean-level errors are shown in the status label.

                // We could show additional messages here if we want, do logging, etc.

            }
        });

    }

    private ValidationResult emailValidator(String s, ValueContext valueContext) {
        if (service.findByEmail(s) != null) {
            return ValidationResult.error("Email already used.");
        }

        return ValidationResult.ok();
    }

    private ValidationResult usernameValidator(String s, ValueContext valueContext) {
        if(service.findByUsername(s) != null) {
            return ValidationResult.error("Username already exist");
        }

        return ValidationResult.ok();
    }

    /**
     * We call this method when form submission has succeeded
     */
    private void showSuccess(User detailsBean) {
        Notification notification = Notification.show("Register Success");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        // Here you'd typically redirect the user to another view
        UI.getCurrent().getPage().setLocation("/login");
    }

    /**
     * Method to validate that:
     * <p>
     * 1) Password is at least 8 characters long
     * <p>
     * 2) Values in both fields match each other
     */

    private ValidationResult passwordValidator(String pass1, ValueContext ctx) {

        /*
         * Just a simple length check. A real version should check for password
         * complexity as well!
         */
        if (pass1 == null || pass1.length() < 8) {
            return ValidationResult.error("Password should be at least 8 characters long");
        }

        if (!enablePasswordValidation) {
            // user hasn't visited the field yet, so don't validate just yet, but next time.
            enablePasswordValidation = true;
            return ValidationResult.ok();
        }

        String pass2 = confirmPassword.getValue();

        if (pass1 != null && pass1.equals(pass2)) {
            return ValidationResult.ok();
        }

        return ValidationResult.error("Passwords do not match");
    }

    /**
     * Custom validator class that extends the built-in email validator.
     * <p>
     * Ths validator checks if the field is visible before performing the
     * validation. This way, the validation is only performed when the user has told
     * us they want marketing emails.
     */

}
