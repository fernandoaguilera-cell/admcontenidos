# API de Reconocimiento Facial con OpenCV

Este proyecto implementa una API REST en Spring Boot que utiliza OpenCV para reconocimiento facial. La aplicación permite comparar dos imágenes y determinar si corresponden al mismo rostro.

## ¿Es posible usar OpenCV para reconocimiento facial?

**Sí, definitivamente es posible usar OpenCV para reconocimiento facial.** OpenCV (Open Source Computer Vision Library) es una de las bibliotecas más populares y potentes para visión por computadora, y ofrece múltiples funcionalidades para reconocimiento facial:

### Capacidades de OpenCV para Reconocimiento Facial:

1. **Detección de Rostros**: Utiliza algoritmos como Haar Cascades y HOG (Histogram of Oriented Gradients) para detectar rostros en imágenes.

2. **Reconocimiento Facial**: Implementa varios algoritmos de reconocimiento:
   - **LBPH (Local Binary Patterns Histograms)**: Algoritmo robusto y eficiente
   - **Eigenfaces**: Basado en análisis de componentes principales (PCA)
   - **Fisherfaces**: Basado en análisis discriminante lineal (LDA)

3. **Comparación de Rostros**: Permite comparar características faciales entre diferentes imágenes para determinar si pertenecen a la misma persona.

## Características del Proyecto

- ✅ Detección automática de rostros en imágenes
- ✅ Comparación de dos imágenes para determinar si son el mismo rostro
- ✅ Cálculo de porcentaje de similitud entre rostros
- ✅ API REST fácil de usar
- ✅ Preprocesamiento de imágenes para mejorar precisión
- ✅ Soporte para múltiples formatos de imagen (JPG, PNG, etc.)

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.1.5**
- **OpenCV 4.7.0** (vía org.openpnp)
- **Maven** (gestión de dependencias)

## Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior

## Instalación y Ejecución

1. **Clonar el repositorio**:
```bash
git clone https://github.com/fernandoaguilera-cell/admcontenidos.git
cd admcontenidos
```

2. **Compilar el proyecto**:
```bash
mvn clean install
```

3. **Ejecutar la aplicación**:
```bash
mvn spring-boot:run
```

La aplicación se ejecutará en `http://localhost:8080`

## Uso de la API

### 1. Verificar el estado del servicio

```bash
curl http://localhost:8080/api/facial-recognition/health
```

**Respuesta**:
```json
{
  "status": "OK",
  "message": "Servicio de reconocimiento facial funcionando correctamente"
}
```

### 2. Comparar dos rostros

**Endpoint**: `POST /api/facial-recognition/compare`

**Parámetros**:
- `image1`: Primera imagen (multipart/form-data)
- `image2`: Segunda imagen (multipart/form-data)

**Ejemplo con cURL**:
```bash
curl -X POST http://localhost:8080/api/facial-recognition/compare \
  -F "image1=@/ruta/a/imagen1.jpg" \
  -F "image2=@/ruta/a/imagen2.jpg"
```

**Respuesta de ejemplo** (mismo rostro):
```json
{
  "success": true,
  "isSameFace": true,
  "similarity": 0.85,
  "similarityPercentage": "85.00%",
  "message": "Las imágenes corresponden al mismo rostro (similitud: 85.00%)"
}
```

**Respuesta de ejemplo** (rostros diferentes):
```json
{
  "success": true,
  "isSameFace": false,
  "similarity": 0.45,
  "similarityPercentage": "45.00%",
  "message": "Las imágenes NO corresponden al mismo rostro (similitud: 45.00%)"
}
```

## Cómo Funciona

### Algoritmo de Comparación

El servicio utiliza un enfoque multi-etapa para comparar rostros:

1. **Detección de Rostros**: 
   - Utiliza el clasificador Haar Cascade de OpenCV
   - Detecta la ubicación del rostro en cada imagen

2. **Preprocesamiento**:
   - Convierte las imágenes a escala de grises
   - Extrae la región de interés (ROI) del rostro
   - Redimensiona a un tamaño estándar (100x100 píxeles)
   - Ecualiza el histograma para mejorar el contraste

3. **Comparación**:
   - Calcula histogramas de ambos rostros
   - Utiliza correlación de histogramas para medir similitud
   - Compara con un umbral predefinido (70%)

4. **Resultado**:
   - Determina si son el mismo rostro
   - Proporciona un porcentaje de similitud

### Umbral de Similitud

El sistema utiliza un umbral de **70%** de similitud:
- **≥ 70%**: Se considera el mismo rostro
- **< 70%**: Se consideran rostros diferentes

Este umbral puede ajustarse en el código según los requisitos específicos de la aplicación.

## Estructura del Proyecto

```
admcontenidos/
├── src/
│   ├── main/
│   │   ├── java/com/admcontenidos/facialrecognition/
│   │   │   ├── FacialRecognitionApplication.java
│   │   │   ├── controller/
│   │   │   │   └── FacialRecognitionController.java
│   │   │   └── service/
│   │   │       └── FacialRecognitionService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── haarcascade_frontalface_default.xml
│   └── test/
│       └── java/com/admcontenidos/facialrecognition/
├── pom.xml
└── README.md
```

## Limitaciones y Consideraciones

1. **Calidad de Imagen**: Las imágenes de mejor calidad producen mejores resultados
2. **Iluminación**: Condiciones de iluminación similares mejoran la precisión
3. **Ángulo**: Las imágenes frontales funcionan mejor que las de perfil
4. **Un Rostro por Imagen**: El sistema procesa el primer rostro detectado
5. **Tamaño de Archivo**: Límite de 10MB por imagen

## Mejoras Potenciales

- Implementar modelos de deep learning (como FaceNet o ArcFace) para mayor precisión
- Agregar soporte para detección de múltiples rostros
- Implementar análisis de rostros en tiempo real con video
- Agregar base de datos para almacenar y comparar con múltiples rostros
- Implementar autenticación y autorización
- Agregar métricas de rendimiento y logging avanzado

## Recursos Adicionales

- [Documentación de OpenCV](https://docs.opencv.org/)
- [OpenCV Face Recognition](https://docs.opencv.org/master/da/d60/tutorial_face_main.html)
- [Haar Cascades](https://docs.opencv.org/master/db/d28/tutorial_cascade_classifier.html)

## Licencia

Este proyecto es una demostración educativa del uso de OpenCV con Spring Boot.

## Autor

Fernando Aguilera
