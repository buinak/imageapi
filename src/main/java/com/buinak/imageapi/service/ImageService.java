package com.buinak.imageapi.service;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.entity.ImageData;
import com.buinak.imageapi.exception.ImageApiRuntimeException;
import com.buinak.imageapi.repository.ImageDataRepository;
import com.buinak.imageapi.repository.ImageRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public Image addImage(String name, String description) {
        ImageData imageData = ImageData.builder().build();
        imageDataRepository.saveAndFlush(imageData);

        return imageRepository.saveAndFlush(Image.builder()
                .name(name)
                .description(description)
                .imageData(imageData)
                .build());
    }

    public Optional<ImageRepository.ImageInformationView> findImageByName(String name) {
        return imageRepository.findByName(name);
    }

    public Image findFullImageById(long id) {
        Image image = imageRepository.findById(id).orElseThrow(ImageApiRuntimeException::new);
        ImageData imageData = image.getImageData();

        return Image.builder()
                .id(image.getId())
                .name(image.getName())
                .description(image.getDescription())
                .imageData(ImageData.builder()
                        .id(imageData.getId())
                        .fullImage(imageData.getFullImage()).build()
                ).build();
    }

    public Optional<ImageRepository.ImageInformationView> patchImage(Image image) {
        Image managedImage = imageRepository.findById(image.getId()).orElseThrow(ImageApiRuntimeException::new);

        managedImage.setName(image.getName());
        managedImage.setDescription(image.getDescription());

        imageRepository.saveAndFlush(managedImage);
        return imageRepository.findByName(image.getName());
    }

    public void deleteImage(long id) {
        Image image = imageRepository.findById(id).orElseThrow(ImageApiRuntimeException::new);

        imageRepository.deleteById(id);
    }
}
