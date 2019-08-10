package com.carrental.Gui;

import com.carrental.invoiceCreator.PDFCreator;
import com.carrental.model.Car;
import com.carrental.model.User;
import com.carrental.repository.CarRepo;
import com.carrental.repository.UserRepo;
import com.carrental.service.MailService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Route("carlist")
public class ListCarGui extends VerticalLayout {

    private CarRepo carRepo;
    private UserRepo userRepo;
    private MailService mailService;

    public ListCarGui(CarRepo carRepo, UserRepo userRepo, MailService mailService) {
        this.carRepo = carRepo;
        this.userRepo = userRepo;
        this.mailService = mailService;

        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();
        Image img = new Image("https://cdn0.iconfinder.com/data/icons/automotive/128/CARS-512.png", "Cars Logo");
        img.setHeight("100px");
        appLayout.setBranding(img);

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.CAR.create(), "Car list", "carlist"));
        }
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CAMERA.create(), "Photos", ""),
                new AppLayoutMenuItem(VaadinIcon.PHONE.create(), "Contact", "contact"));

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Add car", "addcar"));
        }

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Register", "register"));
            AppLayoutMenuItem appLayoutMenuItemLogin = new AppLayoutMenuItem(VaadinIcon.PLUS.create(), "Login");
            appLayoutMenuItemLogin.addMenuItemClickListener(menuItemClickEvent ->
            {
                UI.getCurrent().getPage().executeJavaScript("window.open(\"/login\", \"_self\");");
            });
            menu.addMenuItems(appLayoutMenuItemLogin);
        }
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            AppLayoutMenuItem appLayoutMenuItemLogin = new AppLayoutMenuItem(VaadinIcon.EXIT.create(), "Logout");
            appLayoutMenuItemLogin.addMenuItemClickListener(menuItemClickEvent ->
            {
                UI.getCurrent().getPage().executeJavaScript("window.open(\"/logout\", \"_self\");");
            });
            menu.addMenuItems(appLayoutMenuItemLogin);
        }

        Grid<Car> carGrid = new Grid<>(Car.class);
        List<Car> cars = carRepo.findAll();
        carGrid.setItems(cars);
        carGrid.setHeightFull();

        carGrid.removeColumnByKey("fuel");
        carGrid.removeColumnByKey("yearProduction");
        carGrid.removeColumnByKey("id");
        carGrid.removeColumnByKey("rent");
        carGrid.removeColumnByKey("imageURL");
        carGrid.removeColumnByKey("username");
        carGrid.setColumns("mark", "model", "carType", "price");

        carGrid.setSelectionMode(Grid.SelectionMode.NONE);
        carGrid.setItemDetailsRenderer(TemplateRenderer.<Car>of(
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

        String loggedUser = ((User) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getLogin();
        String loggedUserEmail = ((User) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getEmail();
        String loggedUserName = ((User) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getName();
        String loggedUserSurname = ((User) ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getSurname();

        carGrid.addColumn(new ComponentRenderer<>(car -> {

            Button rentButton = new Button("Rent", event -> {
                if (!car.isRent()) {
                    car.setRent(true);
                    car.setUsername(loggedUser);
                    carRepo.save(car);
                    carGrid.setItems(carRepo.findAll());
                } else if (car.isRent() && car.getUsername().equals(loggedUser)) {
                    try {
                        File file = new File(PDFCreator.DEST);
                        file.getParentFile().mkdirs();
                        (new PDFCreator()).createPdf(PDFCreator.DEST, loggedUserName + " " + loggedUserSurname, car.getMark(), car.getModel(), car.getFuel().toString(), car.getYearProduction(), car.getCarType().toString(), car.getPrice());
                        try {
                            mailService.sendMail(loggedUserEmail, "Billing - Car Rental Company", "New invoice has been generated.<br>\nFind document in the attachment, please.", "Invoice", file, true);
                        } catch (MessagingException var8) {
                            var8.printStackTrace();
                        }
                    } catch (IOException var9) {
                        var9.printStackTrace();
                    }
                    car.setRent(false);
                    car.setUsername(null);
                    carRepo.save(car);
                    carGrid.setItems(carRepo.findAll());
                } else {
                }
            });
            if (!car.isRent()) {
                rentButton.setText("Rent");
            } else if (car.isRent() && car.getUsername().equals(loggedUser)) {
                rentButton.setText("Return");
            } else {
                rentButton.setText("Rented");
                rentButton.setEnabled(false);
            }
            Button removeCar = new Button("Remove");
            removeCar.setVisible(false);
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                removeCar.setVisible(true);
                removeCar.addClickListener(event -> {
                    carRepo.delete(car);
                    carGrid.setItems(carRepo.findAll());
                });
            }

            HorizontalLayout buttons = new HorizontalLayout(rentButton, removeCar);
            return new VerticalLayout(buttons);
        })).setHeader("Actions");

        // Filter Car Type

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

        TextField carTypeTextField = new TextField();
        carTypeTextField.addValueChangeListener(event -> dataProvider.addFilter(
                carType -> StringUtils.containsIgnoreCase(carType.getCarType().toString(),
                        carTypeTextField.getValue())));

        carTypeTextField.setValueChangeMode(ValueChangeMode.EAGER);
        carTypeTextField.setSizeFull();
        carTypeTextField.setPlaceholder("Car Type Filter");

        Details filterPanel = new Details("Filters", new Span(markTextField, modelTextField, carTypeTextField));
        filterPanel.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);

        Component allComponents = new Span(filterPanel, carGrid);
        appLayout.setContent(allComponents);
        add(appLayout);
    }
}