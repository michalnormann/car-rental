package com.carrental.Gui;

import com.carrental.image.ImageUpader;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Route("uploadimage")
public class UploadGui extends Div {

    private ImageUpader imageUpader;


    @Autowired
    public UploadGui(ImageUpader imageUpader) {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setId("upload");
        upload.addSucceededListener(event -> {
            Component component = createComponent(event.getMIMEType(),
                    event.getFileName(),
                    buffer.getInputStream(event.getFileName()));
            add(component);
        });
        add(upload);
    }
    private Component createComponent(String mimeType, String fileName,
                                      InputStream stream) {
        if (!mimeType.startsWith("image/png")) {
            throw new IllegalStateException();
        }
        String text = "";
        try {
            imageUpader.uploadFile(createFile(stream));
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        Div div = new Div();
        div.setText(text);
        div.addClassName("uploaded-text");
        return div;
    }

    public File createFile(InputStream stream) throws IOException {
        File tempFile = File.createTempFile("xxxx", "xxxx");
        tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(stream, out);
        return tempFile;
    }
}