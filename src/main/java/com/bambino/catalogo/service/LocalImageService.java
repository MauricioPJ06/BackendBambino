package com.bambino.catalogo.service;

import com.bambino.shared.exception.NegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
/**
 * Clase que maneja la carga local de imagenes de catalogo.
 */
public class LocalImageService {

    private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    private final Path uploadRoot;

    public LocalImageService(@Value("${app.upload.dir:${UPLOAD_DIR:/app/uploads}}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Metodo que realiza la operacion de uploadProductoImagen.
     * @param archivo parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public String uploadProductoImagen(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new NegocioException("Debes seleccionar una imagen.");
        }
        if (archivo.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new NegocioException("La imagen excede el tamaño máximo de 10MB.");
        }

        String extension = extensionDesdeNombre(archivo.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new NegocioException("Formato de imagen no válido. Usa JPG, PNG o WEBP.");
        }

        Path productosDir = uploadRoot.resolve("productos");
        try {
            Files.createDirectories(productosDir);
            String fileName = UUID.randomUUID() + "." + extension;
            Path destination = productosDir.resolve(fileName).normalize();
            Files.copy(archivo.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/productos/" + fileName;
        } catch (IOException e) {
            throw new NegocioException("No se pudo guardar la imagen en almacenamiento local.");
        }
    }

    private String extensionDesdeNombre(String originalFilename) {
        if (originalFilename == null) {
            throw new NegocioException("El archivo no tiene nombre válido.");
        }
        String name = originalFilename.trim();
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) {
            throw new NegocioException("El archivo debe incluir una extensión válida.");
        }
        return name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}
