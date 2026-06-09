package com.bambino.configuracion.service;

import com.bambino.shared.exception.NegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
/**
 * Clase que maneja la carga local de archivos de configuracion.
 */
public class LocalMediaService {

    private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024;
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> PDF_EXTENSIONS = Set.of("pdf");
    private static final Set<String> VIDEO_EXTENSIONS = Set.of("mp4", "webm", "mov");

    private final Path uploadRoot;

    public LocalMediaService(@Value("${app.upload.dir:${UPLOAD_DIR:/app/uploads}}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Metodo que realiza la operacion de uploadArchivo.
     * @param archivo parametro de entrada para la operacion
     * @param folder parametro de entrada para la operacion
     * @param resourceType parametro de entrada para la operacion
     * @param validarDimensionesImagen parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public UploadResult uploadArchivo(MultipartFile archivo, String folder, String resourceType, boolean validarDimensionesImagen) {
        if (archivo == null || archivo.isEmpty()) {
            throw new NegocioException("Debes seleccionar un archivo.");
        }
        if (archivo.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new NegocioException("El archivo excede el tamaño máximo de 10MB.");
        }

        String extension = extensionDesdeNombre(archivo.getOriginalFilename());
        validarExtension(resourceType, extension);

        String relativeDir = resolveRelativeDir(folder);
        Path targetDir = uploadRoot.resolve(relativeDir).normalize();

        try {
            Files.createDirectories(targetDir);
            String fileName = UUID.randomUUID() + "." + extension;
            Path destination = targetDir.resolve(fileName).normalize();
            Files.copy(archivo.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return new UploadResult("/uploads/" + relativeDir + "/" + fileName, null, null);
        } catch (IOException e) {
            throw new NegocioException("No se pudo guardar el archivo en almacenamiento local.");
        }
    }

    private void validarExtension(String resourceType, String extension) {
        String normalizedType = resourceType == null ? "" : resourceType.trim().toLowerCase(Locale.ROOT);
        boolean valido = switch (normalizedType) {
            case "image" -> IMAGE_EXTENSIONS.contains(extension);
            case "raw" -> PDF_EXTENSIONS.contains(extension);
            case "video" -> VIDEO_EXTENSIONS.contains(extension);
            default -> false;
        };

        if (!valido) {
            String mensaje = switch (normalizedType) {
                case "image" -> "Formato de imagen no válido. Usa JPG, PNG o WEBP.";
                case "raw" -> "Formato de archivo no válido. Solo se permite PDF.";
                case "video" -> "Formato de video no válido. Usa MP4, WEBM o MOV.";
                default -> "Tipo de archivo no soportado.";
            };
            throw new NegocioException(mensaje);
        }
    }

    private String resolveRelativeDir(String folder) {
        String normalized = folder == null ? "" : folder.trim();
        if (normalized.startsWith("bambino/configuracion/")) {
            String clave = normalized.substring("bambino/configuracion/".length());
            return "configuracion/" + sanitizePathSegment(clave);
        }
        return "configuracion";
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

    private String sanitizePathSegment(String value) {
        String ascii = Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        String cleaned = ascii.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_", "")
                .replaceAll("_$", "");
        if (cleaned.isBlank()) {
            throw new NegocioException("La clave del directorio no es válida.");
        }
        return cleaned;
    }

    /**
     * Record que maneja la funcionalidad de UploadResult.
     */
    public record UploadResult(String url, String publicId, String versionTag) {}
}
