package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.entity.ImageData;
import com.buinak.imageapi.repository.ImageDataRepository;
import com.buinak.imageapi.repository.ImageRepository;
import com.buinak.imageapi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
                                          @RequestParam(defaultValue = "DESC") String description){
        return ResponseEntity.ok().body(imageService.addImage(name, description));
    }

    @GetMapping(path = "findImageByName")
    public ResponseEntity<ImageRepository.ImageInformationView> findImageByName(@RequestParam String name){
        Optional<ImageRepository.ImageInformationView> optionalImage = imageService.findImageByName(name);

        if (optionalImage.isPresent()){
            return ResponseEntity.ok().body(optionalImage.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(path = "patchImage")
    public ResponseEntity<ImageRepository.ImageInformationView> patchImage(@RequestBody Image image){
        Optional<ImageRepository.ImageInformationView> optionalImageInformationView = imageService.patchImage(image);
        if (optionalImageInformationView.isEmpty()){
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok(optionalImageInformationView.get());
    }

    @DeleteMapping(path = "deleteImage")
    public ResponseEntity<Long> deleteImage(@RequestParam() long id){
        imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }


}
