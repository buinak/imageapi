package com.buinak.imageapi.service;

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

@Service
public class StorageService {

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

    public void deleteImage(String path){
        File fileToDelete = new File(path);
        fileToDelete.delete();
    }
}
