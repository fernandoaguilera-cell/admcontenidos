package com.admcontenidos.facialrecognition.controller;

import com.admcontenidos.facialrecognition.service.FacialRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para las operaciones de reconocimiento facial
 */
@RestController
@RequestMapping("/api/facial-recognition")
public class FacialRecognitionController {

    @Autowired
    private FacialRecognitionService facialRecognitionService;

    private static final String UPLOAD_DIR = "uploads/";

    /**
     * Endpoint para comparar dos imágenes y determinar si corresponden al mismo rostro
     * 
     * @param file1 Primera imagen
     * @param file2 Segunda imagen
     * @return Resultado de la comparación
     */
    @PostMapping("/compare")
    public ResponseEntity<Map<String, Object>> compareFaces(
            @RequestParam("image1") MultipartFile file1,
            @RequestParam("image2") MultipartFile file2) {
        
        Map<String, Object> response = new HashMap<>();

        try {
            // Crear directorio de uploads si no existe
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Guardar las imágenes temporalmente
            String imagePath1 = saveUploadedFile(file1);
            String imagePath2 = saveUploadedFile(file2);

            // Comparar los rostros
            FacialRecognitionService.FaceComparisonResult result = 
                facialRecognitionService.compareFaces(imagePath1, imagePath2);

            response.put("success", true);
            response.put("isSameFace", result.isSameFace());
            response.put("similarity", result.getSimilarity());
            response.put("similarityPercentage", String.format("%.2f%%", result.getSimilarity() * 100));
            response.put("message", result.getMessage());

            // Limpiar archivos temporales
            deleteFile(imagePath1);
            deleteFile(imagePath2);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint de prueba para verificar que el servicio está funcionando
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Servicio de reconocimiento facial funcionando correctamente");
        return ResponseEntity.ok(response);
    }

    /**
     * Guarda un archivo subido en el sistema de archivos
     */
    private String saveUploadedFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }

    /**
     * Elimina un archivo del sistema de archivos
     */
    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error al eliminar archivo: " + filePath);
        }
    }
}
