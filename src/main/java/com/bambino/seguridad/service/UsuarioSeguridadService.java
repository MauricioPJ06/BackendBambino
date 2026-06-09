package com.bambino.seguridad.service;

import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.seguridad.dto.UsuarioCambiarEstadoRequest;
import com.bambino.seguridad.dto.UsuarioCambiarRolRequest;
import com.bambino.seguridad.dto.UsuarioActualizarRequest;
import com.bambino.seguridad.dto.UsuarioCrearRequest;
import com.bambino.seguridad.dto.RolResponse;
import com.bambino.seguridad.dto.UsuarioResponse;
import com.bambino.seguridad.entity.EstadoUsuario;
import com.bambino.seguridad.entity.RolEntity;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.mapper.UsuarioMapper;
import com.bambino.seguridad.repository.RolRepository;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
/**
 * Clase que maneja la funcionalidad de UsuarioSeguridadService.
 */
public class UsuarioSeguridadService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public UsuarioSeguridadService(
        UsuarioRepository usuarioRepository,
        RolRepository rolRepository,
        UsuarioMapper usuarioMapper,
        PasswordEncoder passwordEncoder,
        AuditoriaService auditoriaService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarUsuarios.
     * @return resultado de la operacion
     */
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAllByOrderByIdUsuarioDesc()
            .stream()
            .map(usuarioMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarRoles.
     * @return resultado de la operacion
     */
    public List<RolResponse> listarRoles() {
        return rolRepository.findAll().stream()
            .sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
            .map(r -> new RolResponse(r.getIdRol(), r.getNombre(), r.getDescripcion()))
            .toList();
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearUsuario.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public UsuarioResponse crearUsuario(UsuarioCrearRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (usuarioRepository.existsByEmail(email)) {
            throw new NegocioException("el email ya existe");
        }
        RolEntity rol = rolRepository.findByNombre(request.rol().trim().toUpperCase(Locale.ROOT))
            .orElseThrow(() -> new NegocioException("rol no existe"));

        LocalDateTime ahora = LocalDateTime.now();

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUuidUsuario(UUID.randomUUID().toString());
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        usuario.setNombres(request.nombres().trim());
        usuario.setApellidos(request.apellidos().trim());
        usuario.setTelefono(request.telefono() == null ? null : request.telefono().trim());
        usuario.setDocumento(generarDocumentoInterno());
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setRol(rol);
        usuario.setFechaCreacion(ahora);
        usuario.setFechaActualizacion(ahora);

        UsuarioEntity guardado = usuarioRepository.save(usuario);
        auditoriaService.registrar(
            "USUARIO",
            String.valueOf(guardado.getIdUsuario()),
            "CREAR_USUARIO",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"email\":\"" + guardado.getEmail() + "\",\"rol\":\"" + guardado.getRol().getNombre() + "\"}"
        );
        return usuarioMapper.toResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de cambiarEstado.
     * @param idUsuario parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public UsuarioResponse cambiarEstado(Long idUsuario, UsuarioCambiarEstadoRequest request) {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new NegocioException("usuario no existe"));
        validarUsuarioEditable(usuario);

        EstadoUsuario nuevoEstado;
        try {
            nuevoEstado = EstadoUsuario.valueOf(request.estado().trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new NegocioException("estado invalido");
        }

        usuario.setEstado(nuevoEstado);
        usuario.setFechaActualizacion(LocalDateTime.now());

        UsuarioEntity guardado = usuarioRepository.save(usuario);
        auditoriaService.registrar(
            "USUARIO",
            String.valueOf(guardado.getIdUsuario()),
            "CAMBIAR_ESTADO_USUARIO",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"estado\":\"" + guardado.getEstado().name() + "\"}"
        );
        return usuarioMapper.toResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de cambiarRol.
     * @param idUsuario parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public UsuarioResponse cambiarRol(Long idUsuario, UsuarioCambiarRolRequest request) {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new NegocioException("usuario no existe"));
        validarUsuarioEditable(usuario);

        String rolNuevoNombre = request.rol().trim().toUpperCase(Locale.ROOT);
        RolEntity rolNuevo = rolRepository.findByNombre(rolNuevoNombre)
            .orElseThrow(() -> new NegocioException("rol no existe"));

        String rolActualNombre = usuario.getRol().getNombre().trim().toUpperCase(Locale.ROOT);
        if (rolActualNombre.equals(rolNuevoNombre)) {
            throw new NegocioException("no hay cambios para guardar");
        }

        if ("CLIENTE".equals(rolActualNombre) && ("COCINA".equals(rolNuevoNombre) || "ADMIN".equals(rolNuevoNombre))) {
            throw new NegocioException("un cliente no puede cambiar a rol cocinero o administrador");
        }

        usuario.setRol(rolNuevo);
        usuario.setFechaActualizacion(LocalDateTime.now());

        UsuarioEntity guardado = usuarioRepository.save(usuario);
        auditoriaService.registrar(
            "USUARIO",
            String.valueOf(guardado.getIdUsuario()),
            "CAMBIAR_ROL_USUARIO",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"rol\":\"" + guardado.getRol().getNombre() + "\"}"
        );
        return usuarioMapper.toResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarDatosUsuario.
     * @param idUsuario parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public UsuarioResponse actualizarDatosUsuario(Long idUsuario, UsuarioActualizarRequest request) {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new NegocioException("usuario no existe"));
        validarUsuarioEditable(usuario);

        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String nombres = request.nombres().trim();
        String apellidos = request.apellidos().trim();
        String telefono = request.telefono() == null ? null : request.telefono().trim();
        if (telefono != null && telefono.isBlank()) telefono = null;

        if (!email.equals(usuario.getEmail().trim().toLowerCase(Locale.ROOT)) && usuarioRepository.existsByEmail(email)) {
            throw new NegocioException("el email ya existe");
        }
        boolean sinCambios =
            usuario.getEmail().trim().equalsIgnoreCase(email) &&
            usuario.getNombres().trim().equals(nombres) &&
            usuario.getApellidos().trim().equals(apellidos) &&
            Objects.equals(normalizarNullable(usuario.getTelefono()), normalizarNullable(telefono));
        if (sinCambios) {
            throw new NegocioException("no hay cambios para guardar");
        }

        usuario.setEmail(email);
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setTelefono(telefono);
        usuario.setFechaActualizacion(LocalDateTime.now());

        UsuarioEntity guardado = usuarioRepository.save(usuario);
        auditoriaService.registrar(
            "USUARIO",
            String.valueOf(guardado.getIdUsuario()),
            "ACTUALIZAR_DATOS_USUARIO",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"nombres\":\"" + guardado.getNombres() + "\",\"apellidos\":\"" + guardado.getApellidos() + "\"}"
        );
        return usuarioMapper.toResponse(guardado);
    }

    private String normalizarNullable(String value) {
        if (value == null) return null;
        String v = value.trim();
        return v.isBlank() ? null : v;
    }

    private void validarUsuarioEditable(UsuarioEntity usuario) {
        String rolActual = usuario.getRol().getNombre().trim().toUpperCase(Locale.ROOT);
        if (!"ADMIN".equals(rolActual) && !"COCINA".equals(rolActual)) {
            throw new NegocioException("solo se permite editar usuarios internos (admin o cocinero)");
        }
    }

    private String generarDocumentoInterno() {
        return "INT" + UUID.randomUUID().toString().replace("-", "").substring(0, 17);
    }

}
