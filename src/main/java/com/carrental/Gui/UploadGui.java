package com.carrental.Gui;

import com.carrental.image.ImageUpader;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("uploadimage")
public class UploadGui extends VerticalLayout {

    private ImageUpader imageUpader;

    @Autowired
    public UploadGui(ImageUpader imageUpader) {
        this.imageUpader = imageUpader;


        Label label = new Label();
        TextField textField = new TextField();
        Button button = new Button("Upload");
        button.addClickListener(event -> {
            String uploadedImage = imageUpader.uploadFile(textField.getValue());
            Image image = new Image(uploadedImage,"nie ma obrazka");
            image.setMaxHeight("200px");
            image.setMaxWidth("200px");
            label.setText("Image added.");
            add(label);
            add(image);
        });

        add(textField, button);
    }
}
