package com.example.application.views.home;

import com.example.application.data.entity.Seminar;
import com.example.application.data.service.SearchminarService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
@Tag("storefront-view")
@PageTitle("Home | Searchminar")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class HomeView extends VerticalLayout {

    @Id("grid")
    private Grid<Seminar> grid = new Grid<>(Seminar.class, false);
    private SearchminarService service;

    TextField filterText = new TextField();
    H1 seminarText = new H1("Seminars Available");
    private String template = "<div class=\"courses-container\">\n" +
            "\t<div class=\"course\">\n" +
            "\t\t<div class=\"course-preview\">\n" +
            "\t\t\t<h6 class='status'>${item.status}</h6>\n" +
            "\t\t\t<h2 class='date'>${item.date}</h2>\n" +
            "\t\t\t<h2 class = 'time'>${item.time}</h2>\n" +
            "\t\t\t<h6 class='address'>${item.address} <i class=\"fas fa-chevron-right\"></i></h6>\n" +
            "\t\t</div>\n" +
            "\t\t<div class=\"course-info\">\n" +
            "\t\t\t<h6 class='price'>${item.price}</h6>\n" +
            "\t\t\t<h2 class='name'>${item.name}</h2>\n" +
            "\t\t\t<div class='description'>${item.description}</div>" +
            "\t\t\t<a href = ${item.link} target=\"_blank\"><button class=\"btn\">More Info</button> </a>\n" +
            "\t\t</div>\n" +
            "\t</div>\n" +
            "</div>";
    public HomeView(SearchminarService service){
        this.service = service;
        configureGrid();
        seminarText.addClassName("seminar-home");
        configureSearchbar();
        add(addToolbar(), grid);

        updateList();
    }

    private Component addToolbar() {
        VerticalLayout toolbar = new VerticalLayout(seminarText, filterText);
        toolbar.addClassName("toolbar");
        toolbar.setAlignItems(Alignment.CENTER);
        return toolbar;
    }

    public void configureGrid(){
        grid.addClassName("grid1");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);

        grid.addColumn(LitRenderer.<Seminar>of(template)
                .withProperty("status", Seminar::getStatus)
                .withProperty("date", Seminar::getDateCustom)
                .withProperty("time", Seminar::getTime)
                .withProperty("address", Seminar::getAddress)
                .withProperty("price", Seminar::getPriceCustom)
                .withProperty("name", Seminar::getName)
                .withProperty("description", Seminar::getDescription)
                .withProperty("link", Seminar::getLink)
        );

    }

    public void configureSearchbar(){
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterText.getStyle().set("width", "25 em");
    }

    public void updateList(){
        grid.setItems(service.findAllSeminars(filterText.getValue()));
    }
}
