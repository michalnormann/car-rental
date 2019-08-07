package com.carrental.Gui;

import com.carrental.model.Car;
import com.carrental.model.User;
import com.carrental.repository.CarRepo;
import com.carrental.repository.UserRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSelectionColumn;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Collection;
import java.util.List;

@Route("carlist")
public class ListCarGui extends VerticalLayout {

    private CarRepo carRepo;
    private UserRepo userRepo;

    public ListCarGui(CarRepo carRepo, UserRepo userRepo) {
        this.carRepo = carRepo;
        this.userRepo = userRepo;

        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://cdn0.iconfinder.com/data/icons/automotive/128/CARS-512.png", "Cars Logo");
        img.setHeight("100px");
        appLayout.setBranding(img);

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.CAR.create(), "Car list", "carlist"));
        }
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CAMERA.create(),"Photos", ""),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(), "Contact", "contact"));

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Add car", "addcar"));
        }


        Grid<Car> carGrid = new Grid<>(Car.class);
        List<Car> cars = carRepo.findAll();
        carGrid.setItems(cars);
        carGrid.setHeightFull();

        carGrid.removeColumnByKey("fuel");
        carGrid.removeColumnByKey("yearProduction");
        carGrid.removeColumnByKey("price");
        carGrid.removeColumnByKey("id");
        carGrid.removeColumnByKey("rent");
        carGrid.removeColumnByKey("imageURL");
        carGrid.removeColumnByKey("username");
        carGrid.setColumns("mark","model","carType");

        carGrid.setSelectionMode(Grid.SelectionMode.NONE);
        carGrid.setItemDetailsRenderer(TemplateRenderer.<Car> of(
                "<div class='custom-details' style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'>"
                        + "<table>\n" +
                        "<tbody>\n" +
                        "<tr>\n" +
                        "<td style=\"width: 300px;\" ><img src=\"[[item.imageURL]]\" style=\"max-height:250px;max-width:250px\"></td>\n" +
                        "<td><div><b>Mark: </b>[[item.mark]]</div>\n" +
                        "<div><b>Model: </b>[[item.model]]</div>\n" +
                        "<div><b>Fuel: </b>[[item.fuel]]</div>\n" +
                        "<div><b>Year of Production: </b>[[item.yearProduction]]</div>\n" +
                        "<div><b>CarType: </b>[[item.carType]]</div>\n" +
                        "<div><b>Price: </b>[[item.price]] zł</div>\n" +
                        "<div><b>Rented By: </b>[[item.username]]</div>\n</td>\n" +
                        "</tr>\n" +
                        "</tbody>\n" +
                        "</table>" +
                        "</div>")
                .withProperty("mark", Car::getMark)
                .withProperty("model", Car::getModel)
                .withProperty("fuel", Car::getFuel)
                .withProperty("yearProduction", Car::getYearProduction)
                .withProperty("carType", Car::getCarType)
                .withProperty("price", Car::getPrice)
                .withProperty("rent", Car::isRent)
                .withProperty("username", Car::getUsername)
                .withProperty("imageURL", Car::getImageURL)
                // This is now how we open the details
                .withEventHandler("handleClick", car -> {
                    carGrid.getDataProvider().refreshItem(car);
                }));

        carGrid.setDetailsVisibleOnClick(false);

        carGrid.addColumn(new NativeButtonRenderer<>("Details", item -> carGrid
                .setDetailsVisible(item, !carGrid.isDetailsVisible(item)))).setHeader("More details");

        ListDataProvider<Car> dataProvider = new ListDataProvider<>(cars);
        carGrid.setDataProvider(dataProvider);

        String loggedUser = ((User)((UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getLogin();

        carGrid.addColumn(new ComponentRenderer<>(car -> {

            Button rentButton = new Button("Rent", event -> {
                if(!car.isRent()) {
                    car.setRent(true);
                    car.setUsername(loggedUser);
                    carRepo.save(car);
                    UI.getCurrent().getPage().reload();
                } else if (car.isRent() && car.getUsername().equals(loggedUser)){
                    car.setRent(false);
                    car.setUsername(null);
                    carRepo.save(car);
                    UI.getCurrent().getPage().reload();
                } else {

                }
            });
            if(!car.isRent()) {
                rentButton.setText("Rent");
            } else if (car.isRent() && car.getUsername().equals(loggedUser)){
                rentButton.setText("Return");
            } else {
                rentButton.setText("Rented");
                rentButton.setEnabled(false);
            }

            HorizontalLayout buttons = new HorizontalLayout(rentButton);
            return new VerticalLayout(buttons);
        })).setHeader("Actions");

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