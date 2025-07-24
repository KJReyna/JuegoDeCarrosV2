package com.tuproyecto.cagaroad.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Clase de utilidad para cargar recursos, como imágenes, desde el classpath del proyecto.
 * Esto es útil para que las imágenes estén empaquetadas con tu archivo JAR final.
 */
public class AssetLoader {

    /**
     * Carga una imagen del directorio de recursos.
     * La imagen debe estar en la carpeta 'resources' (o 'src/main/resources' si usas Maven/Gradle).
     *
     * @param fileName El nombre del archivo de la imagen (ej: "utp_logo.png").
     * @return Un objeto BufferedImage si la carga es exitosa.
     * @throws IOException Si el archivo no se encuentra o hay un error al leer la imagen.
     */
    public static BufferedImage loadImage(String fileName) throws IOException {
        // Usa getResourceAsStream para cargar el recurso desde el classpath.
        // La barra diagonal "/" al principio indica la raíz del classpath.
        InputStream inputStream = AssetLoader.class.getResourceAsStream("/" + fileName);

        if (inputStream == null) {
            // Si el inputStream es nulo, significa que el archivo no se encontró en el classpath.
            throw new IOException("No se pudo encontrar el recurso: " + fileName + ". Asegúrate de que está en la carpeta 'resources'.");
        }

        // Lee la imagen del InputStream
        return ImageIO.read(inputStream);
    }
}