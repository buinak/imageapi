package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(path = "/images")
    public ResponseEntity<Image> addImage(@RequestParam(defaultValue = "NAME") String name,
                                          @RequestParam(defaultValue = "DESC") String description,
                                          @RequestParam() MultipartFile file) {
        return ResponseEntity.ok().body(imageService.addImage(name, description, file));
    }

    @GetMapping(path = "/images/links")
    public ResponseEntity<List<String>> getImageLinks(){
        return ResponseEntity.ok().body(imageService.listImageUrls());
    }

    @GetMapping(path = "/images/{id}")
    public ResponseEntity<Image> findImageById(@PathVariable Long id){
        return ResponseEntity.ok(imageService.findImageById(id));
    }

    @PatchMapping(path = "/images/{id}")
    public ResponseEntity<Image> patchImage(@PathVariable Long id, @RequestBody Image image){
        return ResponseEntity.ok(imageService.patchImage(id, image));
    }

    @DeleteMapping(path = "/images/{id}")
    public ResponseEntity<?> deleteImageById(@PathVariable long id){
        imageService.deleteImage(id);
        return new ResponseEntity<>(id, HttpStatus.ACCEPTED);
    }
}
