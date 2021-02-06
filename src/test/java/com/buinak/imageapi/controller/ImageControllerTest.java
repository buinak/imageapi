package com.buinak.imageapi.controller;

import com.buinak.imageapi.entity.Image;
import com.buinak.imageapi.repository.ImageDataRepository;
import com.buinak.imageapi.repository.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(JUnit4.class)
public class ImageControllerTest {

    private final ImageRepository imageRepository = Mockito.mock(ImageRepository.class);
    private final ImageDataRepository imageDataRepository = Mockito.mock(ImageDataRepository.class);

    private final ImageController target = new ImageController(imageRepository, imageDataRepository);

    @Test
    public void whenImageByIdDoesNotExist_patchShouldReturn404() {
        Mockito.when(imageRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        ResponseEntity responseEntity = target.patchImage(Image.builder().id(1L).build());
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void whenPatchIsCalled_correctImageIsSavedFromParameters() {
        Mockito.when(imageRepository.findById(Mockito.any())).thenReturn(
                Optional.of(
                        Image.builder()
                                .id(1L)
                                .name("TEST")
                                .description("TEST_TEST")
                                .build()));

        Mockito.when(imageRepository.findByName(Mockito.any())).thenReturn(Optional.of(new ImageRepository.ImageInformationView() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }
        }));

        target.patchImage(Image.builder()
                .id(1L)
                .name("NEWNAME")
                .description("NEWDESC")
                .build());

        Mockito.verify(imageRepository, Mockito.times(1))
                .saveAndFlush(
                        Mockito.argThat(image -> image.getName().equals("NEWNAME") && image.getDescription().equals("NEWDESC")));

    }

}