package com.bambino.seguridad.controller;

import com.bambino.seguridad.dto.PerfilInternoActualizarRequest;
import com.bambino.seguridad.dto.PerfilInternoCambiarPasswordRequest;
import com.bambino.seguridad.dto.PerfilInternoResponse;
import com.bambino.seguridad.service.PerfilInternoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/seguridad/perfil")
/**
 * Clase que maneja la funcionalidad de PerfilInternoController.
 */
public class PerfilInternoController {

    private final PerfilInternoService perfilInternoService;

    public PerfilInternoController(PerfilInternoService perfilInternoService) {
        this.perfilInternoService = perfilInternoService;
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de obtenerPerfil.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PerfilInternoResponse obtenerPerfil(Authentication authentication) {
        return perfilInternoService.obtenerPerfil(authentication.getName());
    }

    @PatchMapping("/datos-personales")
    public PerfilInternoResponse actualizarDatos(
        Authentication authentication,
        @Valid @RequestBody PerfilInternoActualizarRequest request
    ) {
        return perfilInternoService.actualizarDatos(authentication.getName(), request);
    }

    @PatchMapping("/password")
    public String cambiarPassword(
        Authentication authentication,
        @Valid @RequestBody PerfilInternoCambiarPasswordRequest request
    ) {
        return perfilInternoService.cambiarPassword(authentication.getName(), request);
    }
}
