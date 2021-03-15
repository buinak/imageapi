package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.repository.ImageRepository;
import com.buinak.imageapi.service.StorageService;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ImageControllerIT {

    @Autowired
    ImageController imageController;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    StorageService storageService;

    final static String IMG_PATH = "src/test/java/com/buinak/imageapi/controller/testimg.jpeg";


    @Test
    public void addImage() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("testimg.jpeg", new FileInputStream(IMG_PATH));

        imageController.addImage("NAME1", "DESC1", multipartFile);

        Image image= imageController.findImageByName("NAME1").getBody();
        assertThat(image.getName()).isEqualTo("NAME1");
        assertThat(image.getDescription()).isEqualTo("DESC1");
    }

    @Test
    public void listImages() throws IOException {
        MockMultipartFile firstFile = new MockMultipartFile("data", "testimg.jpeg", "text/plain", "some xml".getBytes());
        imageController.addImage("NAME1", "DESC1", firstFile);

        MockMultipartFile secondFile = new MockMultipartFile("data", "testimg.jpeg", "text/plain", "some xml".getBytes());
        imageController.addImage("NAME2", "DESC2", secondFile);

        List<String> images = imageController.getImageLinks().getBody();
        System.out.println("-----------------------------------------------");
        System.out.println(imageRepository.findByName("NAME1"));
        System.out.println(images);
        assertThat(images.size()).isEqualTo(2);
        assertThat(images.get(0)).isEqualTo("testimg.jpeg");
        assertThat(images.get(1)).isEqualTo("testimg2.jpeg");
    }

    @Test
    public void patchImage() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("data", "testimg.jpeg", "text/plain", "some xml".getBytes());
        ResponseEntity<Image> responseImage = imageController.addImage("NAME2", "DESC2", multipartFile);
        Image image = responseImage.getBody();
        image.setDescription("string");
        image.setName("string");

        imageController.patchImage(image);

        Image image2 = imageController.findImageByName("string").getBody();
        assertThat(image2.getName()).isEqualTo("string");
        assertThat(image2.getDescription()).isEqualTo("string");


    }

    @Test
    public void deleteImage() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("data", "testimg.jpeg", "text/plain", "some xml".getBytes());
        ResponseEntity<Image> responseImage = imageController.addImage("NAME3", "DESC3", multipartFile);
        Image image = responseImage.getBody();
        long id = image.getId();

        assertThat(imageController.findImageByName("NAME3").getBody()).isNotNull();
        imageController.deleteImageById(id);
        assertThat(imageController.findImageByName("NAME3").getBody()).isNull();
    }
}
