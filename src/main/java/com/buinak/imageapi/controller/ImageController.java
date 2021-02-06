package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("/image")
public class ImageController {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @PostMapping(path = "addImage")
    public ResponseEntity<Image> addImage(){
        Image image = Image.builder()
                .label("TEST_IMAGE")
                .description("TEST_DESC")
                .path("TEST_PATH")
                .build();

        return ResponseEntity.ok().body(imageRepository.saveAndFlush(image));
    }
}
