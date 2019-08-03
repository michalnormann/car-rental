package com.carrental.Gui;

import com.carrental.model.Car;
import com.carrental.repository.CarRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

@Route("")
public class ListCarGui extends VerticalLayout {

    private CarRepo carRepo;

    public ListCarGui(CarRepo carRepo) {
        this.carRepo = carRepo;

        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://cdn0.iconfinder.com/data/icons/automotive/128/CARS-512.png", "Cars Logo");
        img.setHeight("100px");
        appLayout.setBranding(img);


        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CAR.create(), "Car list", ""),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(), "Contact", "contact"),
                new AppLayoutMenuItem(VaadinIcon.CAMERA.create(),"Fotos", "fotos"));

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Add car", "addcar"));
        }


        Grid<Car> carGrid = new Grid<>(Car.class);
        List<Car> cars = carRepo.findAll();
        carGrid.setItems(cars);
        carGrid.setHeightFull();

//
        carGrid.setSelectionMode(Grid.SelectionMode.NONE);

// You can use any renderer for the item details. By default, the
// details are opened and closed by clicking the rows.
        carGrid.setItemDetailsRenderer(TemplateRenderer.<Car> of(
                "<div class='custom-details' style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'>"
                        + "<div><b>Mark: </b>[[item.mark]]</div>"
                        + "<div><b>Model: </b>[[item.model]]</div>"
                        + "<div><b>Fuel: </b>[[item.fuel]]</div>"
                        + "<div><b>Year of Production: </b>[[item.yearProduction]]</div>"
                        + "<div><b>CarType: </b>[[item.carType]]</div>"
                        + "<div><b>Price: </b>[[item.price]] zł</div>"
                        + "</div>")
                .withProperty("mark", Car::getMark)
                .withProperty("model", Car::getModel)
                .withProperty("fuel", Car::getFuel)
                .withProperty("yearProduction", Car::getYearProduction)
                .withProperty("carType", Car::getCarType)
                .withProperty("price", Car::getPrice)
                // This is now how we open the details
                .withEventHandler("handleClick", car -> {
                    carGrid.getDataProvider().refreshItem(car);
                }));

// Disable the default way of opening item details:
        carGrid.setDetailsVisibleOnClick(false);

        carGrid.addColumn(new NativeButtonRenderer<>("Details", item -> carGrid
                .setDetailsVisible(item, !carGrid.isDetailsVisible(item)))).setHeader("Actions");
//
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
}