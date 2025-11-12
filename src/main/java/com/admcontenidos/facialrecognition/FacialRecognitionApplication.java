package com.admcontenidos.facialrecognition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación Spring Boot para reconocimiento facial usando OpenCV
 */
@SpringBootApplication
public class FacialRecognitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacialRecognitionApplication.class, args);
    }
}
