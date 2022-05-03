package com.example.application.views.list;

import com.example.application.data.entity.Seminar;
import com.example.application.data.service.SearchminarService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PageTitle("Admin | Searchminar")
@Route(value = "/admin", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ListView extends VerticalLayout {
    Grid<Seminar> grid = new Grid<>(Seminar.class);
    TextField filterText = new TextField();
    SeminarForm form;

    SearchminarService service;

    public ListView(SearchminarService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();
        
        add(
          getToolBar(), getContent()
        );

        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setSeminar(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllSeminars(filterText.getValue()));
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addSeminarButton = new Button("Add Seminar");
        addSeminarButton.addClickListener(e -> addSeminar());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addSeminarButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addSeminar() {
        grid.asSingleSelect().clear();
        editSeminar(new Seminar());
    }

    private Component getContent(){
        HorizontalLayout content = new HorizontalLayout(grid,form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }
    private void configureForm(){
        form = new SeminarForm();
        form.setWidth("25em");

        form.addListener(SeminarForm.SaveEvent.class, this::saveSeminar);
        form.addListener(SeminarForm.DeleteEvent.class, this::deleteSeminar);
        form.addListener(SeminarForm.CloseEvent.class, e -> closeEditor());
    }
    private void saveSeminar(SeminarForm.SaveEvent event){
        service.saveSeminar(event.getSeminar());
        updateList();
        closeEditor();
    }
    private void deleteSeminar(SeminarForm.DeleteEvent event){
        service.deleteSeminar(event.getSeminar());
        updateList();
        closeEditor();
    }
    private void configureGrid() {
        grid.addClassName("seminar-grid");
        grid.setSizeFull();
        grid.setColumns("name", "address","description", "date", "price", "status", "link");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editSeminar(e.getValue()));

    }

    private void editSeminar(Seminar seminar) {
        if(seminar == null){
            closeEditor();
        }else{
            form.setSeminar(seminar);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}
