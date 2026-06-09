package com.bambino.clientes.controller;

import com.bambino.clientes.dto.DireccionRequest;
import com.bambino.clientes.dto.DireccionResponse;
import com.bambino.clientes.service.ClienteDireccionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cliente/direcciones")
/**
 * Clase que maneja la funcionalidad de ClienteDireccionController.
 */
public class ClienteDireccionController {

    private final ClienteDireccionService clienteDireccionService;

    public ClienteDireccionController(ClienteDireccionService clienteDireccionService) {
        this.clienteDireccionService = clienteDireccionService;
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de listarDirecciones.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<DireccionResponse> listarDirecciones(Authentication authentication) {
        return clienteDireccionService.listarDirecciones(authentication.getName());
    }

    @GetMapping("/principal")
    /**
     * Metodo que realiza la operacion de obtenerPrincipal.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public DireccionResponse obtenerPrincipal(Authentication authentication) {
        return clienteDireccionService.obtenerDireccionPrincipal(authentication.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DireccionResponse crearDireccion(
        Authentication authentication,
        @Valid @RequestBody DireccionRequest request
    ) {
        return clienteDireccionService.crearDireccion(authentication.getName(), request);
    }

    @PutMapping("/{idDireccion}")
    public DireccionResponse actualizarDireccion(
        Authentication authentication,
        @PathVariable Long idDireccion,
        @Valid @RequestBody DireccionRequest request
    ) {
        return clienteDireccionService.actualizarDireccion(authentication.getName(), idDireccion, request);
    }

    @PatchMapping("/{idDireccion}/principal")
    /**
     * Metodo que realiza la operacion de marcarPrincipal.
     * @param authentication parametro de entrada para la operacion
     * @param idDireccion parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public DireccionResponse marcarPrincipal(Authentication authentication, @PathVariable Long idDireccion) {
        return clienteDireccionService.marcarPrincipal(authentication.getName(), idDireccion);
    }

    @DeleteMapping("/{idDireccion}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    /**
     * Metodo que realiza la operacion de eliminarDireccion.
     * @param authentication parametro de entrada para la operacion
     * @param idDireccion parametro de entrada para la operacion
     */
    public void eliminarDireccion(Authentication authentication, @PathVariable Long idDireccion) {
        clienteDireccionService.eliminarDireccion(authentication.getName(), idDireccion);
    }
}
