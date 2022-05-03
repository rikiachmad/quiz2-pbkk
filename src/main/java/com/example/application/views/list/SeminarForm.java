package com.example.application.views.list;

import com.example.application.data.entity.Seminar;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;

public class SeminarForm extends FormLayout {
    Binder<Seminar> binder = new BeanValidationBinder<>(Seminar.class);

    TextField name = new TextField("Name");
    TextField address = new TextField("Address");

    TextArea description = new TextArea("Description");
    DateTimePicker date = new DateTimePicker("Seminar Starting Time");

    IntegerField price = new IntegerField("Price");
    Select<String> status = new Select<>();

    TextField link = new TextField("Link");

    // use textfield for image path for temporary only

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    private Seminar seminar;

    public SeminarForm(){
        addClassName("seminar-form");
        date.setMin(LocalDateTime.now());
        status.setLabel("Status");
        status.setItems("Offline", "Online");
        address.setMaxLength(60);
        name.setMaxLength(100);
        description.setMaxLength(200);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 200);
        });

        binder.bind(name, "name");
        binder.bind(address, "address");
        binder.bind(description, "description");
        binder.bind(date, "date");
        binder.bind(price, "price");

        binder.bind(status, "status");
        binder.bind(link, "link");


        add(
          name, address, description, price, date, status,link, createButtonLayout()
        );

    }

    public void setSeminar(Seminar seminar){
        this.seminar = seminar;
        binder.readBean(seminar);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, seminar)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(seminar);
            fireEvent(new SaveEvent(this, seminar));
        }catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class SeminarFormEvent extends ComponentEvent<SeminarForm> {
        private Seminar seminar;

        protected SeminarFormEvent(SeminarForm source, Seminar seminar) {
            super(source, false);
            this.seminar = seminar;
        }

        public Seminar getSeminar() {
            return seminar;
        }
    }

    public static class SaveEvent extends SeminarFormEvent {
        SaveEvent(SeminarForm source, Seminar seminar) {
            super(source, seminar);
        }
    }

    public static class DeleteEvent extends SeminarFormEvent {
        DeleteEvent(SeminarForm source, Seminar seminar) {
            super(source, seminar);
        }

    }

    public static class CloseEvent extends SeminarFormEvent {
        CloseEvent(SeminarForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
