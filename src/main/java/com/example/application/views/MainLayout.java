package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.home.HomeView;
import com.example.application.views.list.ListView;
import com.example.application.views.list.UsersView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class MainLayout extends AppLayout {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    private SecurityService securityService;
    private RouterLink userView = new RouterLink("Users", UsersView.class);
    private RouterLink listView = new RouterLink("List", ListView.class);
    private RouterLink homeView = new RouterLink("Home", HomeView.class);
    public MainLayout(SecurityService securityService){
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    public void createHeader(){
        Image logo = new Image("/images/logo_searchminar.png", "Logo");
        logo.addClassNames("logo","text-m", "m-m");

        Button logOut = new Button("Log out", e -> securityService.logout());
        logOut.addClassName("logout");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logOut);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");


        addToNavbar(header);
    }

    public void createDrawer(){
        Tabs tabs = getTabs();
        addToDrawer(tabs);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.DASHBOARD, "Home", homeView)
        );
        if(userDetails.getAuthorities().iterator().next().toString().equals("ROLE_ADMIN")) {
            tabs.add(
                createTab(VaadinIcon.LIST, "List", listView),
                createTab(VaadinIcon.USER_HEART, "Users", userView)
            );
        }
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, RouterLink link) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("padding", "var(--lumo-space-xs)");

        link.add(icon);
        link.setTabIndex(-1);
        return new Tab(link);
    }
}
