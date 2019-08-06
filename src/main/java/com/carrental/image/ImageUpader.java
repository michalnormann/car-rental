package com.carrental.image;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageUpader {

    private Cloudinary cloudinary;

    public ImageUpader() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dqxizarbt",
                "api_key", "376875919551663",
                "api_secret", "sqenKC4rVJQvoFlu-_gRNmbpaqs"));
    }

    public String uploadFile(String path) {
        File file = new File(path);
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        } catch (IOException e) {
            // todo
        }
        return uploadResult.get("url").toString();
    }
}
