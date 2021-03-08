package com.buinak.imageapi.service;


import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.exception.ImageApiRuntimeException;
import com.buinak.imageapi.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Service
public class StorageService {

    ImageRepository imageRepository;

    @Autowired
    public StorageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Value("${app.upload.dir:static}")
    public String uploadDir;

    public String uploadFile(MultipartFile file) {

        String fileLocation = uploadDir + File.separator + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            Path copyLocation = Paths
                    .get(fileLocation);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileLocation;
    }

    public void deleteImage(Long id){
        Image managedImage = imageRepository.findById(id).get();
        String path = managedImage.getPath();
        File fileToDelete = new File(String.valueOf(path));
        fileToDelete.delete();
    }
}
