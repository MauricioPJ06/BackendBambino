package com.bambino.seguridad.service;

import com.bambino.seguridad.dto.PerfilInternoActualizarRequest;
import com.bambino.seguridad.dto.PerfilInternoCambiarPasswordRequest;
import com.bambino.seguridad.dto.PerfilInternoResponse;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

@Service
/**
 * Clase que maneja la funcionalidad de PerfilInternoService.
 */
public class PerfilInternoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public PerfilInternoService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerPerfil.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PerfilInternoResponse obtenerPerfil(String emailAutenticado) {
        UsuarioEntity usuario = obtenerUsuarioInterno(emailAutenticado);
        return toResponse(usuario);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarDatos.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PerfilInternoResponse actualizarDatos(String emailAutenticado, PerfilInternoActualizarRequest request) {
        UsuarioEntity usuario = obtenerUsuarioInterno(emailAutenticado);

        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String nombres = request.nombres().trim();
        String apellidos = request.apellidos().trim();
        String telefono = request.telefono() == null ? null : request.telefono().trim();
        if (telefono != null && telefono.isBlank()) telefono = null;

        String emailActual = usuario.getEmail().trim().toLowerCase(Locale.ROOT);
        if (!email.equals(emailActual) && usuarioRepository.existsByEmail(email)) {
            throw new NegocioException("el email ya existe");
        }

        boolean sinCambios =
            emailActual.equals(email)
                && usuario.getNombres().trim().equals(nombres)
                && usuario.getApellidos().trim().equals(apellidos)
                && Objects.equals(normalizarNullable(usuario.getTelefono()), normalizarNullable(telefono));
        if (sinCambios) {
            throw new NegocioException("no hay cambios para guardar");
        }

        usuario.setEmail(email);
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setTelefono(telefono);
        usuario.setFechaActualizacion(LocalDateTime.now());

        UsuarioEntity guardado = usuarioRepository.save(usuario);
        return toResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de cambiarPassword.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public String cambiarPassword(String emailAutenticado, PerfilInternoCambiarPasswordRequest request) {
        UsuarioEntity usuario = obtenerUsuarioInterno(emailAutenticado);

        String actual = request.passwordActual().trim();
        String nueva = request.passwordNueva().trim();
        String confirmacion = request.passwordConfirmacion().trim();

        if (!passwordEncoder.matches(actual, usuario.getPasswordHash())) {
            throw new NegocioException("passwordActual incorrecta");
        }
        if (!nueva.equals(confirmacion)) {
            throw new NegocioException("passwordConfirmacion no coincide");
        }
        if (actual.equals(nueva)) {
            throw new NegocioException("passwordNueva debe ser diferente a la actual");
        }
        if (passwordEncoder.matches(nueva, usuario.getPasswordHash())) {
            throw new NegocioException("passwordNueva debe ser diferente a la actual");
        }

        usuario.setPasswordHash(passwordEncoder.encode(nueva));
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        return "password actualizada correctamente";
    }

    private UsuarioEntity obtenerUsuarioInterno(String emailAutenticado) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));
        String rol = usuario.getRol().getNombre().trim().toUpperCase(Locale.ROOT);
        if (!"ADMIN".equals(rol) && !"COCINA".equals(rol)) {
            throw new NegocioException("solo se permite perfil para usuarios internos (admin o cocina)");
        }
        return usuario;
    }

    private PerfilInternoResponse toResponse(UsuarioEntity usuario) {
        return new PerfilInternoResponse(
            usuario.getIdUsuario(),
            usuario.getEmail(),
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getTelefono(),
            usuario.getRol().getNombre(),
            usuario.getEstado().name()
        );
    }

    private String normalizarNullable(String value) {
        if (value == null) return null;
        String v = value.trim();
        return v.isBlank() ? null : v;
    }
}
