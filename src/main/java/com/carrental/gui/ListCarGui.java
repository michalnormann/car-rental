package com.carrental.gui;
import com.carrental.model.Car;
import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
@Route("list-car")
public class ListCarGui extends VerticalLayout {

    private CarRepo carRepo;
    
    public ListCarGui(CarRepo carRepo) {
        this.carRepo = carRepo;

        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://i.imgur.com/GPpnszs.png", "Car Rental");
        img.setHeight("44px");
        appLayout.setBranding(img);

        menu.addMenuItems(new AppLayoutMenuItem(VaadinIcon.PLUS.create(),"Add car", "addcar"),
                new AppLayoutMenuItem(VaadinIcon.CAR.create(),"Car list", "list-car"),
                new AppLayoutMenuItem("Page 3", "page3"),
                new AppLayoutMenuItem("Page 4", "page4"));

        Component content = new Span(new H3("Add new car"),
                new Span("Page content"));
        appLayout.setContent(content);

// Commit

        Tab carList = new Tab("Car list");
        Tab login = new Tab("Login");
        Tab contact = new Tab("Contact");
        Tabs tabs = new Tabs(carList, login, contact);
//
//        Tab tab1 = new Tab("Tab one");
//        Div page1 = new Div();
//        page1.setText("Page#1");
//
//        Tab tab2 = new Tab("Tab two");
//        Div page2 = new Div();
//        page2.setText("Page#2");
//        page2.setVisible(false);
//
//        Tab tab3 = new Tab("Tab three");
//        Div page3 = new Div();
//        page3.setText("Page#3");
//        page3.setVisible(false);
//
//        Map<Tab, Component> tabsToPages = new HashMap<>();
//        tabsToPages.put(tab1, page1);
//        tabsToPages.put(tab2, page2);
//        tabsToPages.put(tab3, page3);
//        Tabs tabs = new Tabs(tab1, tab2, tab3);
//        Div pages = new Div(page1, page2, page3);
//        Set<Component> pagesShown = Stream.of(page1)
//                .collect(Collectors.toSet());
//
//        tabs.addSelectedChangeListener(event -> {
//            pagesShown.forEach(page -> page.setVisible(false));
//            pagesShown.clear();
//            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
//            selectedPage.setVisible(true);
//            pagesShown.add(selectedPage);
//        });

        Grid<Car> carGrid = new Grid<>(Car.class);
        carGrid.setItems(carRepo.findAll());

        carGrid.removeColumnByKey("fuel");
        carGrid.removeColumnByKey("yearProduction");
        carGrid.removeColumnByKey("price");

        add(appLayout);
        add(tabs);
        add(carGrid);
    }
}