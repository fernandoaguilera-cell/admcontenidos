package com.admcontenidos.facialrecognition.example;

import com.admcontenidos.facialrecognition.service.FacialRecognitionService;
import nu.pattern.OpenCV;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Ejemplo de uso del servicio de reconocimiento facial
 * 
 * Esta clase demuestra cómo usar el servicio para comparar dos imágenes
 * y determinar si corresponden al mismo rostro.
 */
public class FacialRecognitionExample {

    public static void main(String[] args) {
        // Cargar OpenCV
        OpenCV.loadLocally();
        
        // Crear instancia del servicio
        FacialRecognitionService service = new FacialRecognitionService();
        
        // Nota: En este ejemplo necesitarías configurar manualmente el detector
        // En la aplicación Spring Boot, esto se hace automáticamente con @PostConstruct
        
        System.out.println("=== Ejemplo de Reconocimiento Facial con OpenCV ===\n");
        
        System.out.println("Para usar este servicio:");
        System.out.println("1. Proporciona dos rutas de imágenes");
        System.out.println("2. El servicio detectará rostros en ambas imágenes");
        System.out.println("3. Comparará los rostros y determinará si son la misma persona");
        System.out.println("4. Retornará un porcentaje de similitud\n");
        
        System.out.println("Ejemplo de código:");
        System.out.println("```java");
        System.out.println("FacialRecognitionService.FaceComparisonResult result =");
        System.out.println("    service.compareFaces(\"/ruta/imagen1.jpg\", \"/ruta/imagen2.jpg\");");
        System.out.println("");
        System.out.println("System.out.println(\"¿Mismo rostro? \" + result.isSameFace());");
        System.out.println("System.out.println(\"Similitud: \" + result.getSimilarity());");
        System.out.println("System.out.println(\"Mensaje: \" + result.getMessage());");
        System.out.println("```\n");
        
        System.out.println("=== Características de OpenCV para Reconocimiento Facial ===\n");
        
        System.out.println("1. Detección de Rostros:");
        System.out.println("   - Haar Cascades: Rápido y eficiente");
        System.out.println("   - HOG (Histogram of Oriented Gradients): Más preciso");
        System.out.println("   - DNN (Deep Neural Networks): Más moderno\n");
        
        System.out.println("2. Algoritmos de Reconocimiento:");
        System.out.println("   - LBPH (Local Binary Patterns Histograms)");
        System.out.println("   - Eigenfaces (basado en PCA)");
        System.out.println("   - Fisherfaces (basado en LDA)\n");
        
        System.out.println("3. Técnicas de Comparación:");
        System.out.println("   - Correlación de histogramas");
        System.out.println("   - Distancia euclidiana");
        System.out.println("   - Chi-cuadrado");
        System.out.println("   - Intersección de histogramas\n");
        
        System.out.println("=== Respuesta a la Pregunta ===\n");
        
        System.out.println("¿Es posible usar OpenCV para reconocimiento facial?");
        System.out.println("✅ SÍ, absolutamente posible y ampliamente utilizado.\n");
        
        System.out.println("OpenCV proporciona:");
        System.out.println("- Detección de rostros en imágenes y video");
        System.out.println("- Múltiples algoritmos de reconocimiento facial");
        System.out.println("- Herramientas para entrenar modelos personalizados");
        System.out.println("- Capacidad de comparar rostros entre imágenes");
        System.out.println("- Alto rendimiento y eficiencia");
        System.out.println("- Soporte multiplataforma (Windows, Linux, Mac, Android, iOS)\n");
        
        System.out.println("Este proyecto demuestra cómo usar OpenCV en Java con Spring Boot");
        System.out.println("para comparar dos imágenes y determinar si son el mismo rostro.");
    }
}
