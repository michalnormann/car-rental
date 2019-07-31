package com.carrental.gui;

import com.carrental.model.Car;
import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Route("list-car")
public class ListCarGui extends VerticalLayout {

    private CarRepo carRepo;
    
    public ListCarGui(CarRepo carRepo) {
        this.carRepo = carRepo;
        Tab tabview = new Tab();
        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://cdn0.iconfinder.com/data/icons/automotive/128/CARS-512.png", "Cars Logo");
        img.setHeight("100px");
        appLayout.setBranding(img);


        menu.addMenuItems(new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Add car", "addcar"),
                new AppLayoutMenuItem(VaadinIcon.CAR.create(), "Car list", "list-car"),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(), "Contact", "contact"),
                new AppLayoutMenuItem(VaadinIcon.CAMERA.create(),"Fotos", "fotos"));


        Grid<Car> carGrid = new Grid<>(Car.class);
        List<Car> cars = carRepo.findAll();
        carGrid.setItems(cars);
        carGrid.addComponentColumn(item -> detailsButton(carGrid, item))
                .setHeader("Actions");
        ListDataProvider<Car> dataProvider = new ListDataProvider<>(cars);
        carGrid.setDataProvider(dataProvider);
        carGrid.removeColumnByKey("fuel");
        carGrid.removeColumnByKey("yearProduction");
        carGrid.removeColumnByKey("price");
        carGrid.removeColumnByKey("id");

        // Filter Car Type
        TextField carTypeTextField = new TextField();
        carTypeTextField.addValueChangeListener(event -> dataProvider.addFilter(
                carType -> StringUtils.containsIgnoreCase(carType.getCarType().toString(),
                        carTypeTextField.getValue())));

        carTypeTextField.setValueChangeMode(ValueChangeMode.EAGER);
        carTypeTextField.setSizeFull();
        carTypeTextField.setPlaceholder("Car Type Filter");

        TextField markTextField = new TextField();
        markTextField.addValueChangeListener(event -> dataProvider.addFilter(
                carType -> StringUtils.containsIgnoreCase(carType.getMark(),
                        markTextField.getValue())));

        markTextField.setValueChangeMode(ValueChangeMode.EAGER);
        markTextField.setSizeFull();
        markTextField.setPlaceholder("Mark Filter");

        TextField modelTextField = new TextField();
        modelTextField.addValueChangeListener(event -> dataProvider.addFilter(
                carType -> StringUtils.containsIgnoreCase(carType.getModel(),
                        modelTextField.getValue())));

        modelTextField.setValueChangeMode(ValueChangeMode.EAGER);
        modelTextField.setSizeFull();
        modelTextField.setPlaceholder("Model Filter");

        Details filterPanel = new Details("Filters",new Span(carTypeTextField,markTextField,modelTextField));
        filterPanel.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);

        Component allComponents = new Span(filterPanel,carGrid);
        appLayout.setContent(allComponents);
        add(appLayout);


    }

    private Button detailsButton(Grid<Car> carGrid, Car car) {
        Button detailsButton = new Button("Details", clickEvent -> {
            ListDataProvider<Car> dataProvider = (ListDataProvider) carGrid
                    .getDataProvider();
            dataProvider.getItems().contains(car);
            dataProvider.refreshAll();
        });
        return detailsButton;
    }
}