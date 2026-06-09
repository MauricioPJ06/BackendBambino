package com.bambino.seguridad.controller;

import com.bambino.seguridad.dto.PerfilActualResponse;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
/**
 * Clase que maneja la funcionalidad de AutenticacionController.
 */
public class AutenticacionController {

    private final UsuarioRepository usuarioRepository;

    public AutenticacionController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/ping")
    /**
     * Metodo que realiza la operacion de ping.
     * @return resultado de la operacion
     */
    public String ping() {
        return "ok";
    }

    @GetMapping("/yo")
    @PreAuthorize("isAuthenticated()")
    /**
     * Metodo que realiza la operacion de perfilActual.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PerfilActualResponse perfilActual(Authentication authentication) {
        List<String> roles = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        UsuarioEntity usuario = usuarioRepository.findByEmail(authentication.getName()).orElse(null);
        String nombres = usuario != null ? usuario.getNombres() : null;
        String apellidos = usuario != null ? usuario.getApellidos() : null;
        String telefono = usuario != null ? usuario.getTelefono() : null;

        return new PerfilActualResponse(authentication.getName(), nombres, apellidos, telefono, roles);
    }
}
