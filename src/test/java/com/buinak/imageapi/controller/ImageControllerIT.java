package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.repository.ImageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ImageControllerIT {

    @Autowired
    ImageController imageController;
    
    @Test
    public void addImage_addsAnImage(){
        imageController.addImage("NAME", "DESC").getBody();

        ImageRepository.ImageInformationView imageInformationView = imageController.findImageByName("NAME").getBody();
        assertThat(imageInformationView.getName()).isEqualTo("NAME");
        assertThat(imageInformationView.getDescription()).isEqualTo("DESC");
    }
}
