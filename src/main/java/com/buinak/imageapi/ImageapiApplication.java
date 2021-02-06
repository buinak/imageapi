package com.buinak.imageapi;

import com.buinak.imageapi.entity.Image;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class ImageapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageapiApplication.class, args);
    }

}
