package com.bambino.seguridad.controller;

import com.bambino.seguridad.dto.UsuarioCambiarEstadoRequest;
import com.bambino.seguridad.dto.UsuarioCambiarRolRequest;
import com.bambino.seguridad.dto.UsuarioActualizarRequest;
import com.bambino.seguridad.dto.UsuarioCrearRequest;
import com.bambino.seguridad.dto.RolResponse;
import com.bambino.seguridad.dto.UsuarioResponse;
import com.bambino.seguridad.service.UsuarioSeguridadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/seguridad/usuarios")
/**
 * Clase que maneja la funcionalidad de SeguridadAdminController.
 */
public class SeguridadAdminController {

    private final UsuarioSeguridadService usuarioSeguridadService;

    public SeguridadAdminController(UsuarioSeguridadService usuarioSeguridadService) {
        this.usuarioSeguridadService = usuarioSeguridadService;
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de listarUsuarios.
     * @return resultado de la operacion
     */
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioSeguridadService.listarUsuarios();
    }

    @GetMapping("/roles")
    /**
     * Metodo que realiza la operacion de listarRoles.
     * @return resultado de la operacion
     */
    public List<RolResponse> listarRoles() {
        return usuarioSeguridadService.listarRoles();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de crearUsuario.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public UsuarioResponse crearUsuario(@Valid @RequestBody UsuarioCrearRequest request) {
        return usuarioSeguridadService.crearUsuario(request);
    }

    @PatchMapping("/{idUsuario}/estado")
    public UsuarioResponse cambiarEstado(
        @PathVariable Long idUsuario,
        @Valid @RequestBody UsuarioCambiarEstadoRequest request
    ) {
        return usuarioSeguridadService.cambiarEstado(idUsuario, request);
    }

    @PatchMapping("/{idUsuario}")
    public UsuarioResponse actualizarDatosUsuario(
        @PathVariable Long idUsuario,
        @Valid @RequestBody UsuarioActualizarRequest request
    ) {
        return usuarioSeguridadService.actualizarDatosUsuario(idUsuario, request);
    }

    @PatchMapping("/{idUsuario}/rol")
    public UsuarioResponse cambiarRol(
        @PathVariable Long idUsuario,
        @Valid @RequestBody UsuarioCambiarRolRequest request
    ) {
        return usuarioSeguridadService.cambiarRol(idUsuario, request);
    }
}
