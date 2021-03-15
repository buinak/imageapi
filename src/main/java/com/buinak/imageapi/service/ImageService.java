package com.buinak.imageapi.service;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.exception.ImageApiRuntimeException;
import com.buinak.imageapi.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final StorageService storageService;


    @Autowired
    public ImageService(ImageRepository imageRepository, StorageService storageService) {
        this.imageRepository = imageRepository;
        this.storageService = storageService;
    }

    public Image addImage(String name, String description, MultipartFile file) {

        String path = storageService.uploadFile(file);

        Image image = Image.builder()
                .name(name)
                .description(description)
                .path(path)
                .build();

        return imageRepository.saveAndFlush(image);
    }

    public List<String> listImageUrls() {
        final String baseUrl =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return imageRepository.findAll().stream()
                .map(image -> baseUrl + image.getPath())
                .collect(Collectors.toList());
    }

    public Optional<Image> findImageById(long id) {
        return imageRepository.findById(id);
    }

    public Optional<Image> findImageByName(String name) {
        return imageRepository.findByName(name);
    }

    public Optional<Image> patchImage(Image image) {
        Image managedImage = imageRepository.findById(image.getId()).orElseThrow(ImageApiRuntimeException::new);

        managedImage.setName(image.getName());
        managedImage.setDescription(image.getDescription());

        imageRepository.saveAndFlush(managedImage);
        return imageRepository.findByName(image.getName());
    }

    public void deleteImage(long id) {
        Image managedImage = imageRepository.findById(id).get();
        String path = managedImage.getPath();
        storageService.deleteImage(path);
        imageRepository.deleteById(id);
    }
}
