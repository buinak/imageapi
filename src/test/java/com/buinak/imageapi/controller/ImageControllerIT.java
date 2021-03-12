package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.repository.ImageRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ImageControllerIT {

    @Autowired
    ImageController imageController;

    @Autowired
    ImageRepository imageRepository;

    final static String IMG_PATH = "src/test/java/com/buinak/imageapi/controller/testimg.jpeg";


    @Test
    public void addImage() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("testimg.jpeg", new FileInputStream(new File(IMG_PATH)));

        imageController.addImage("NAME1", "DESC1", multipartFile);

        ImageRepository.ImageInformationView imageInformationView = imageController.findImageByName("NAME1").getBody();
        assertThat(imageInformationView.getName()).isEqualTo("NAME1");
        assertThat(imageInformationView.getDescription()).isEqualTo("DESC1");
    }

    @Test
    public void listImages() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("testimg.jpeg", new FileInputStream(new File(IMG_PATH)));
        MultipartFile multipartFile2 = new MockMultipartFile("testimg2.jpeg", new FileInputStream(new File(IMG_PATH)));
        imageController.addImage("NAME1", "DESC1", multipartFile);
        imageController.addImage("NAME2", "DESC2", multipartFile2);

        List<String> images = imageController.getImageLinks().getBody();
        assertThat(images.size()).isEqualTo(2);
        assertThat(images.get(1)).isEqualTo("testimg.jpeg");
        assertThat(images.get(2)).isEqualTo("testimg2.jpeg");
    }

    @Test
    public void patchImage() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("testimg.jpeg", new FileInputStream(new File(IMG_PATH)));
        ResponseEntity<Image> responseImage = imageController.addImage("NAME2", "DESC2", multipartFile);
        Image image = responseImage.getBody();
        image.setDescription("string");
        image.setName("string");

        imageController.patchImage(image);

        ImageRepository.ImageInformationView imageInformationView = imageController.findImageByName("string").getBody();
        assertThat(imageInformationView.getName()).isEqualTo("string");
        assertThat(imageInformationView.getDescription()).isEqualTo("string");
    }

    @Test
    public void deleteImage() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("testimg.jpeg", new FileInputStream(new File(IMG_PATH)));
        ResponseEntity<Image> responseImage = imageController.addImage("NAME3", "DESC3", multipartFile);
        Image image = responseImage.getBody();
        long id = image.getId();

        assertThat(imageController.findImageByName("NAME3").getBody()).isNotNull();
        imageController.deleteImageById(id);
        assertThat(imageController.findImageByName("NAME3").getBody()).isNull();
    }
}
