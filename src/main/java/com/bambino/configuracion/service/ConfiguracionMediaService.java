package com.bambino.configuracion.service;

import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.configuracion.dto.ConfiguracionMediaRequest;
import com.bambino.configuracion.dto.ConfiguracionMediaResponse;
import com.bambino.configuracion.entity.ConfiguracionMediaEntity;
import com.bambino.configuracion.entity.TipoMedia;
import com.bambino.configuracion.repository.ConfiguracionMediaRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
/**
 * Clase que maneja la funcionalidad de ConfiguracionMediaService.
 */
public class ConfiguracionMediaService {

    private final ConfiguracionMediaRepository configuracionMediaRepository;
    private final LocalMediaService localMediaService;
    private final AuditoriaService auditoriaService;

    public ConfiguracionMediaService(ConfiguracionMediaRepository configuracionMediaRepository,
                                     LocalMediaService localMediaService,
                                     AuditoriaService auditoriaService) {
        this.configuracionMediaRepository = configuracionMediaRepository;
        this.localMediaService = localMediaService;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarPublicasActivas.
     * @return resultado de la operacion
     */
    public List<ConfiguracionMediaResponse> listarPublicasActivas() {
        return configuracionMediaRepository.findByActivaTrueOrderByClaveAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerPublicaPorClave.
     * @param clave parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ConfiguracionMediaResponse obtenerPublicaPorClave(String clave) {
        ConfiguracionMediaEntity entity = buscarPorClave(clave);
        if (!Boolean.TRUE.equals(entity.getActiva())) {
            throw new NegocioException("La configuración solicitada no está activa.");
        }
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarAdmin.
     * @return resultado de la operacion
     */
    public List<ConfiguracionMediaResponse> listarAdmin() {
        return configuracionMediaRepository.findAll().stream()
                .sorted((a, b) -> a.getClave().compareToIgnoreCase(b.getClave()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerAdminPorClave.
     * @param clave parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ConfiguracionMediaResponse obtenerAdminPorClave(String clave) {
        return toResponse(buscarPorClave(clave));
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarPorClave.
     * @param clave parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ConfiguracionMediaResponse actualizarPorClave(String clave, ConfiguracionMediaRequest request) {
        ConfiguracionMediaEntity entity = buscarPorClave(clave);
        TipoMedia tipo = parseTipo(request.tipo());
        String urlNormalizada = normalizarUrl(request.url());
        if (!mediaTieneCambios(entity, request, tipo, urlNormalizada)) {
            throw new NegocioException("no hay cambios para guardar");
        }
        entity.setNombre(request.nombre().trim());
        entity.setDescripcion(limpiar(request.descripcion()));
        entity.setTipo(tipo);
        entity.setUrl(urlNormalizada);
        entity.setPublicId(limpiar(request.publicId()));
        entity.setVersionTag(limpiar(request.versionTag()));
        entity.setActiva(request.activa());
        entity.setFechaActualizacion(LocalDateTime.now());
        ConfiguracionMediaEntity saved = configuracionMediaRepository.save(entity);
        auditoriaService.registrar(
                "CONFIGURACION_MEDIA",
                String.valueOf(saved.getIdMedia()),
                "ACTUALIZAR_CONFIGURACION_MEDIA",
                AuditoriaActorTipo.ADMIN,
                null,
                AuditoriaCanal.WEB,
                "{\"clave\":\"" + saved.getClave() + "\",\"tipo\":\"" + saved.getTipo().name() + "\"}"
        );
        return toResponse(saved);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de subirArchivoPorClave.
     * @param clave parametro de entrada para la operacion
     * @param archivo parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ConfiguracionMediaResponse subirArchivoPorClave(String clave, MultipartFile archivo) {
        ConfiguracionMediaEntity entity = buscarPorClave(clave);
        String resourceType = switch (entity.getTipo()) {
            case PDF -> "raw";
            case VIDEO -> "video";
            case IMAGEN -> "image";
        };
        String folder = "bambino/configuracion/" + entity.getClave().toLowerCase(Locale.ROOT);
        LocalMediaService.UploadResult upload = localMediaService.uploadArchivo(
                archivo,
                folder,
                resourceType,
                entity.getTipo() == TipoMedia.IMAGEN
        );
        entity.setUrl(upload.url());
        entity.setPublicId(upload.publicId());
        entity.setVersionTag(upload.versionTag());
        entity.setFechaActualizacion(LocalDateTime.now());
        ConfiguracionMediaEntity saved = configuracionMediaRepository.save(entity);
        auditoriaService.registrar(
                "CONFIGURACION_MEDIA",
                String.valueOf(saved.getIdMedia()),
                "SUBIR_ARCHIVO_CONFIGURACION_MEDIA",
                AuditoriaActorTipo.ADMIN,
                null,
                AuditoriaCanal.WEB,
                "{\"clave\":\"" + saved.getClave() + "\"}"
        );
        return toResponse(saved);
    }

    private ConfiguracionMediaEntity buscarPorClave(String clave) {
        String key = clave == null ? "" : clave.trim();
        if (key.isBlank()) {
            throw new NegocioException("La clave es obligatoria.");
        }
        return configuracionMediaRepository.findByClave(key)
                .orElseThrow(() -> new NegocioException("No existe configuración media para la clave indicada."));
    }

    private TipoMedia parseTipo(String value) {
        try {
            return TipoMedia.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new NegocioException("Tipo de media inválido.");
        }
    }

    private String normalizarUrl(String value) {
        String url = value == null ? "" : value.trim();
        if (url.isBlank()) {
            return "";
        }
        try {
            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
                throw new NegocioException("La URL debe usar http o https.");
            }
            return url;
        } catch (IllegalArgumentException e) {
            throw new NegocioException("La URL no es válida.");
        }
    }

    private String limpiar(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private boolean mediaTieneCambios(ConfiguracionMediaEntity entity, ConfiguracionMediaRequest request, TipoMedia tipo, String urlNormalizada) {
        return !strEq(entity.getNombre(), request.nombre())
                || !strEqNull(entity.getDescripcion(), request.descripcion())
                || entity.getTipo() != tipo
                || !strEq(entity.getUrl(), urlNormalizada)
                || !strEqNull(entity.getPublicId(), request.publicId())
                || !strEqNull(entity.getVersionTag(), request.versionTag())
                || !java.util.Objects.equals(entity.getActiva(), request.activa());
    }

    private boolean strEq(String left, String right) {
        String l = left == null ? "" : left.trim();
        String r = right == null ? "" : right.trim();
        return l.equals(r);
    }

    private boolean strEqNull(String left, String right) {
        return java.util.Objects.equals(limpiar(left), limpiar(right));
    }

    private ConfiguracionMediaResponse toResponse(ConfiguracionMediaEntity e) {
        return new ConfiguracionMediaResponse(
                e.getIdMedia(),
                e.getClave(),
                e.getNombre(),
                e.getDescripcion(),
                e.getTipo().name(),
                e.getUrl(),
                e.getPublicId(),
                e.getVersionTag(),
                e.getActiva()
        );
    }
}
