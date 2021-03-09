package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.repository.ImageRepository;
import com.buinak.imageapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping(path = "findImageById")
    public ResponseEntity<Image> findImageById(@RequestParam Long id){
        Optional<Image> optionalImage = imageService.findImageById(id);

        return optionalImage.map(imageInformationView -> ResponseEntity.ok().body(imageInformationView)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "findImageByName")
    public ResponseEntity<ImageRepository.ImageInformationView> findImageByName(@RequestParam String name){
        Optional<ImageRepository.ImageInformationView> optionalImage = imageService.findImageByName(name);

        return optionalImage.map(imageInformationView -> ResponseEntity.ok().body(imageInformationView)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(path = "patchImage")
    public ResponseEntity<Optional<ImageRepository.ImageInformationView>> patchImage(@RequestBody Image image){
        return ResponseEntity.ok(imageService.patchImage(image));
    }

    @DeleteMapping(path = "deleteImageById")
    public ResponseEntity<?> deleteImageById(@RequestParam long id){
        Optional<Image> optionalImage = Optional.ofNullable(imageService.deleteImage(id));
        return optionalImage.map(imageInformationView -> ResponseEntity.ok().body(imageInformationView)).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
