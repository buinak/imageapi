package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.repository.ImageRepository;
import com.buinak.imageapi.service.ImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
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
    ImageService imageService;


    @Test
    public void addImage() {
        MockMultipartFile multipartFile = new MockMultipartFile("data", "testimg.jpeg", "text/plain", "some xml".getBytes());

        imageController.addImage("NAME1", "DESC1", multipartFile);

        Image image= imageService.findImageByName("NAME1");
        assertThat(image.getName()).isEqualTo("NAME1");
        assertThat(image.getDescription()).isEqualTo("DESC1");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void listImages() {
        //setup
        final String BASE_URL =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        MockMultipartFile firstFile = new MockMultipartFile("data", "testimg.jpeg", "text/plain", "some xml".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("data2", "testimg2.jpeg", "text/plain", "some xml".getBytes());

        //action
        imageController.addImage("list", "list", firstFile);
        imageController.addImage("list2", "list2", secondFile);

        //assert
        List<String> images = imageController.getImageLinks().getBody();
        assertThat(images.size()).isEqualTo(2);
        assertThat(images.get(0)).isEqualTo(BASE_URL + File.separator + "testimg.jpeg");
        assertThat(images.get(1)).isEqualTo(BASE_URL + File.separator + "testimg2.jpeg");

    }

    @Test
    public void patchImage() {
        MockMultipartFile multipartFile = new MockMultipartFile("data", "testimg.jpeg", "text/plain", "some xml".getBytes());
        ResponseEntity<Image> responseImage = imageController.addImage("NAME2", "DESC2", multipartFile);
        Image image = responseImage.getBody();
        image.setDescription("string");
        image.setName("string");

        imageController.patchImage(image.getId(), image);

        Image image2 = imageService.findImageByName("string");
        assertThat(image2.getName()).isEqualTo("string");
        assertThat(image2.getDescription()).isEqualTo("string");
    }

    @Test
    public void deleteImage() {
        MockMultipartFile multipartFile = new MockMultipartFile("data", "testimg.jpeg", "text/plain", "some xml".getBytes());
        ResponseEntity<Image> responseImage = imageController.addImage("NAME3", "DESC3", multipartFile);
        Image image = responseImage.getBody();
        long id = image.getId();

        assertThat(imageService.findImageByName("NAME3")).isNotNull();
        imageController.deleteImageById(id);
        assertThat(imageService.findImageByName("NAME3")).isNull();
    }
}
