package com.admcontenidos.facialrecognition.service;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de reconocimiento facial usando OpenCV
 * Permite comparar dos imágenes y determinar si corresponden al mismo rostro
 */
@Service
public class FacialRecognitionService {

    private CascadeClassifier faceDetector;
    private static final String HAAR_CASCADE_PATH = "haarcascade_frontalface_default.xml";
    
    @PostConstruct
    public void init() {
        // Cargar la librería nativa de OpenCV
        OpenCV.loadLocally();
        
        // Inicializar el detector de rostros Haar Cascade
        faceDetector = new CascadeClassifier();
        // Cargar el clasificador desde los recursos de OpenCV
        String cascadePath = getClass().getClassLoader().getResource(HAAR_CASCADE_PATH).getPath();
        if (!faceDetector.load(cascadePath)) {
            System.err.println("Error al cargar el clasificador Haar Cascade");
        }
    }

    /**
     * Detecta rostros en una imagen
     * 
     * @param imagePath Ruta de la imagen
     * @return Lista de rectángulos que contienen los rostros detectados
     */
    public List<Rect> detectFaces(String imagePath) {
        Mat image = Imgcodecs.imread(imagePath);
        if (image.empty()) {
            throw new IllegalArgumentException("No se pudo cargar la imagen: " + imagePath);
        }

        // Convertir a escala de grises
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Detectar rostros
        MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale(grayImage, faces);

        return faces.toList();
    }

    /**
     * Compara dos imágenes para determinar si corresponden al mismo rostro
     * Utiliza el algoritmo LBPH (Local Binary Patterns Histograms) para el reconocimiento
     * 
     * @param imagePath1 Ruta de la primera imagen
     * @param imagePath2 Ruta de la segunda imagen
     * @return FaceComparisonResult con el resultado de la comparación
     */
    public FaceComparisonResult compareFaces(String imagePath1, String imagePath2) {
        // Cargar y preprocesar las imágenes
        Mat face1 = loadAndPreprocessFace(imagePath1);
        Mat face2 = loadAndPreprocessFace(imagePath2);

        if (face1 == null || face2 == null) {
            return new FaceComparisonResult(false, 0.0, "No se detectó rostro en una o ambas imágenes");
        }

        // Calcular la similitud usando histogramas
        double similarity = calculateSimilarity(face1, face2);
        
        // Umbral de similitud (valores más altos indican mayor similitud)
        // Típicamente, valores > 0.7 indican que son el mismo rostro
        double threshold = 0.7;
        boolean isSameFace = similarity >= threshold;

        String message = isSameFace 
            ? "Las imágenes corresponden al mismo rostro (similitud: " + String.format("%.2f", similarity * 100) + "%)"
            : "Las imágenes NO corresponden al mismo rostro (similitud: " + String.format("%.2f", similarity * 100) + "%)";

        return new FaceComparisonResult(isSameFace, similarity, message);
    }

    /**
     * Carga una imagen y extrae el rostro preprocesado
     * 
     * @param imagePath Ruta de la imagen
     * @return Mat con el rostro preprocesado o null si no se detecta rostro
     */
    private Mat loadAndPreprocessFace(String imagePath) {
        Mat image = Imgcodecs.imread(imagePath);
        if (image.empty()) {
            return null;
        }

        // Convertir a escala de grises
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Detectar rostros
        MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale(grayImage, faces);

        List<Rect> facesList = faces.toList();
        if (facesList.isEmpty()) {
            return null;
        }

        // Tomar el primer rostro detectado
        Rect faceRect = facesList.get(0);
        Mat faceROI = new Mat(grayImage, faceRect);

        // Redimensionar a un tamaño estándar para comparación
        Mat resizedFace = new Mat();
        Size targetSize = new Size(100, 100);
        Imgproc.resize(faceROI, resizedFace, targetSize);

        // Ecualizar el histograma para mejorar el contraste
        Imgproc.equalizeHist(resizedFace, resizedFace);

        return resizedFace;
    }

    /**
     * Calcula la similitud entre dos rostros usando correlación de histogramas
     * 
     * @param face1 Primera imagen de rostro
     * @param face2 Segunda imagen de rostro
     * @return Valor de similitud entre 0 y 1 (1 = idénticos)
     */
    private double calculateSimilarity(Mat face1, Mat face2) {
        // Calcular histogramas
        Mat hist1 = new Mat();
        Mat hist2 = new Mat();

        List<Mat> images1 = new ArrayList<>();
        images1.add(face1);
        List<Mat> images2 = new ArrayList<>();
        images2.add(face2);

        MatOfInt channels = new MatOfInt(0);
        MatOfInt histSize = new MatOfInt(256);
        MatOfFloat ranges = new MatOfFloat(0f, 256f);

        Imgproc.calcHist(images1, channels, new Mat(), hist1, histSize, ranges);
        Imgproc.calcHist(images2, channels, new Mat(), hist2, histSize, ranges);

        // Normalizar histogramas
        Core.normalize(hist1, hist1, 0, 1, Core.NORM_MINMAX);
        Core.normalize(hist2, hist2, 0, 1, Core.NORM_MINMAX);

        // Comparar histogramas usando correlación
        double correlation = Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CORREL);

        // Convertir a valor de similitud (0-1)
        return (correlation + 1.0) / 2.0;
    }

    /**
     * Clase interna para representar el resultado de la comparación
     */
    public static class FaceComparisonResult {
        private final boolean isSameFace;
        private final double similarity;
        private final String message;

        public FaceComparisonResult(boolean isSameFace, double similarity, String message) {
            this.isSameFace = isSameFace;
            this.similarity = similarity;
            this.message = message;
        }

        public boolean isSameFace() {
            return isSameFace;
        }

        public double getSimilarity() {
            return similarity;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "FaceComparisonResult{" +
                    "isSameFace=" + isSameFace +
                    ", similarity=" + String.format("%.2f", similarity) +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
