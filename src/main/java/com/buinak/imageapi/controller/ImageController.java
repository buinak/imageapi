package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller("/image")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(path = "addImage")
    public ResponseEntity<Image> addImage(@RequestParam(defaultValue = "NAME") String name,
                                          @RequestParam(defaultValue = "DESC") String description,
                                          @RequestParam() MultipartFile file) {
        return ResponseEntity.ok().body(imageService.addImage(name, description, file));
    }

    @GetMapping(path = "getImageLinks")
    public ResponseEntity<List> getImageLinks(){
        List<String> imageUrls = imageService.listImageUrls();
        return ResponseEntity.ok().body(imageUrls);
    }

    @GetMapping(path = "findImageById")
    public ResponseEntity<Image> findImageById(@RequestParam Long id){
        Optional<Image> optionalImage = imageService.findImageById(id);

        return optionalImage.map(imageInformationView -> ResponseEntity.ok().body(imageInformationView)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "findImageByName")
    public ResponseEntity<Image> findImageByName(String name){
        Optional<Image> optionalImage = imageService.findImageByName(name);

        return optionalImage.map(imageInformationView -> ResponseEntity.ok().body(imageInformationView)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(path = "patchImage")
    public ResponseEntity<Optional<Image>> patchImage(@RequestBody Image image){
        return ResponseEntity.ok(imageService.patchImage(image));
    }

    @DeleteMapping(path = "deleteImageById")
    public ResponseEntity<?> deleteImageById(@RequestParam long id){
        imageService.deleteImage(id);
        return new ResponseEntity<>(id, HttpStatus.ACCEPTED);
    }
}
