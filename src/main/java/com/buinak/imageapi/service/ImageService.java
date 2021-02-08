package com.buinak.imageapi.service;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.entity.ImageData;
import com.buinak.imageapi.exception.ImageApiRuntimeException;
import com.buinak.imageapi.repository.ImageDataRepository;
import com.buinak.imageapi.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageDataRepository imageDataRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, ImageDataRepository imageDataRepository) {
        this.imageRepository = imageRepository;
        this.imageDataRepository = imageDataRepository;
    }

    public Image addImage(String name, String description){
        ImageData imageData = ImageData.builder().build();
        imageDataRepository.saveAndFlush(imageData);

        return imageRepository.saveAndFlush(Image.builder()
                .name(name)
                .description(description)
                .imageData(imageData)
                .build());
    }

    public Optional<ImageRepository.ImageInformationView> findImageByName(String name){
        return imageRepository.findByName(name);
    }

    public Optional<ImageRepository.ImageInformationView> patchImage(Image image){
        Optional<Image> optionalImage = imageRepository.findById(image.getId());

        if (optionalImage.isEmpty()){
            throw new ImageApiRuntimeException();
        }

        Image managedImage = optionalImage.get();
        managedImage.setName(image.getName());
        managedImage.setDescription(image.getDescription());

        imageRepository.saveAndFlush(managedImage);
        return imageRepository.findByName(image.getName());
    }

    public void deleteImage(long id){
        Optional<Image> optionalImage = imageRepository.findById(id);

        if (optionalImage.isEmpty()){
            throw new ImageApiRuntimeException();
        }

        Image managedImage = optionalImage.get();
        ImageData imageData = managedImage.getImageData();
        long imageDataId = imageData.getId();
        imageRepository.deleteById(id);
        imageDataRepository.deleteById(imageDataId);

    }
}
