package com.buinak.imageapi.repository;

import com.buinak.imageapi.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
