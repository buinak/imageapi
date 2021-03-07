package com.buinak.imageapi.service;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.exception.ImageApiRuntimeException;
import com.buinak.imageapi.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

    public Optional<ImageRepository.ImageInformationView> findImageByName(String name) {
        return imageRepository.findByName(name);
    }

//    public List<Image> findPage(int pageNumber, int pageSize){
//        Page<Image> imagePage = imageRepository.findAll(PageRequest.of(pageNumber, pageSize));
//        return imagePage.getContent();
//    }


    public Optional<ImageRepository.ImageInformationView> patchImage(Image image) {
        Image managedImage = imageRepository.findById(image.getId()).orElseThrow(ImageApiRuntimeException::new);

        managedImage.setName(image.getName());
        managedImage.setDescription(image.getDescription());

        imageRepository.saveAndFlush(managedImage);
        return imageRepository.findByName(image.getName());
    }

    public void deleteImage(long id) {
        imageRepository.deleteById(id);
        imageRepository.deleteById(id);
    }
}
