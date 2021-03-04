package com.buinak.imageapi.controller;

import com.buinak.imageapi.repository.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ImageControllerIT {

    @Autowired
    ImageController imageController;

    String testImgPath = "src/test/java/com/buinak/imageapi/controller/testimg.jpeg";


    @Test
    public void checkImgValues() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("testimg.jpeg", new FileInputStream(new File(testImgPath)));
        imageController.addImage("NAME1", "DESC1", multipartFile).getBody();

        ImageRepository.ImageInformationView imageInformationView = imageController.findImageByName("NAME1").getBody();
        assertThat(imageInformationView.getName()).isEqualTo("NAME1");
        assertThat(imageInformationView.getDescription()).isEqualTo("DESC1");
    }

    @Test
    public void deleteImage() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("testimg.jpeg", new FileInputStream(new File(testImgPath)));
        imageController.addImage("NAME1", "DESC1", multipartFile).getBody();

        ImageRepository.ImageInformationView imageInformationView = imageController.findImageByName("NAME1").getBody();
        imageController.deleteImage(imageInformationView.getId());
        assertThat(imageController.findImageByName("NAME1").getBody()).isNull();
    }
}

