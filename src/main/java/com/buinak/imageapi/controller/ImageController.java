package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.repository.ImageRepository;
import com.buinak.imageapi.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller("/image")
public class ImageController {

    private final ImageRepository imageRepository;
    private final StorageService storageService;

    @Autowired
    public ImageController(ImageRepository imageRepository,
                           StorageService storageService) {
        this.imageRepository = imageRepository;
        this.storageService = storageService;
    }

    @PostMapping(path = "addImage")
    public ResponseEntity<Image> addImage(@RequestParam(defaultValue = "NAME") String name,
                                          @RequestParam(defaultValue = "DESC") String description,
                                          @RequestParam() MultipartFile file) {

        String path = storageService.uploadFile(file);

        Image image = Image.builder()
                .name(name)
                .description(description)
                .path(path)
                .build();

        return ResponseEntity.ok().body(imageRepository.saveAndFlush(image));
    }

    @GetMapping(path = "findImageByName")
    public ResponseEntity<ImageRepository.ImageInformationView> findImageByName(@RequestParam String name){
        Optional<ImageRepository.ImageInformationView> optionalImage = imageRepository.findByName(name);

        return optionalImage.map(imageInformationView -> ResponseEntity.ok().body(imageInformationView)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(path = "patchImage")
    public ResponseEntity<ImageRepository.ImageInformationView> patchImage(@RequestBody Image image){
        Optional<Image> optionalImage = imageRepository.findById(image.getId());

        if (optionalImage.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Image managedImage = optionalImage.get();
        managedImage.setName(image.getName());
        managedImage.setDescription(image.getDescription());

        imageRepository.saveAndFlush(managedImage);
        return ResponseEntity.ok(imageRepository.findByName(image.getName()).orElseThrow());
    }

    @DeleteMapping(path = "deleteImage")
    public ResponseEntity<?> deleteImage(@RequestBody long id){
        Optional<Image> optionalImage = imageRepository.findById(id);

        if (optionalImage.isEmpty()){
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }

        Image imageToDelete = optionalImage.get();
        storageService.deleteImage(imageToDelete.getPath());
        imageRepository.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.ACCEPTED);
    }
}
