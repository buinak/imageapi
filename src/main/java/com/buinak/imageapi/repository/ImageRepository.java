package com.buinak.imageapi.repository;

import com.buinak.imageapi.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<ImageInformationView> findByName(String name);


    interface ImageInformationView {
        long getId();
        String getName();
        String getDescription();
    }
}
