package com.example.application.views.list;

import com.example.application.data.entity.Seminar;
import com.example.application.data.entity.User;
import com.example.application.data.service.SearchminarService;
import com.example.application.data.service.UserService;
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

import javax.annotation.security.RolesAllowed;

@PageTitle("UsersList | Searchminar")
@Route(value = "/userlist", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class UsersView extends VerticalLayout {
    Grid<User> grid = new Grid<>(User.class);
    TextField filterText = new TextField();
    private UserService service;

    public UsersView(UserService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        add(
                getToolbar(),
                getContent()
        );
        updateList();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by username...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        return filterText;
    }
    private Component getContent(){
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void updateList() {
        grid.setItems(service.findAllUsers(filterText.getValue()));
    }

    private void configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.setColumns("username", "email", "password", "role");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }
}
