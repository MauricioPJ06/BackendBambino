package com.bambino.clientes.controller;

import com.bambino.clientes.dto.ActualizarDocumentoRequest;
import com.bambino.clientes.dto.ActualizarDatosPersonalesRequest;
import com.bambino.clientes.dto.CambiarPasswordRequest;
import com.bambino.clientes.dto.ClienteDocumentoRequest;
import com.bambino.clientes.dto.ClienteDocumentoResponse;
import com.bambino.clientes.dto.ClientePerfilResponse;
import com.bambino.clientes.service.ClientePerfilService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cliente/perfil")
/**
 * Clase que maneja la funcionalidad de ClientePerfilController.
 */
public class ClientePerfilController {

    private final ClientePerfilService clientePerfilService;

    public ClientePerfilController(ClientePerfilService clientePerfilService) {
        this.clientePerfilService = clientePerfilService;
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de obtenerPerfil.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ClientePerfilResponse obtenerPerfil(Authentication authentication) {
        return clientePerfilService.obtenerPerfil(authentication.getName());
    }

    @GetMapping("/documentos")
    /**
     * Metodo que realiza la operacion de listarDocumentos.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<ClienteDocumentoResponse> listarDocumentos(Authentication authentication) {
        return clientePerfilService.listarDocumentos(authentication.getName());
    }

    @PostMapping("/documentos")
    public ClienteDocumentoResponse agregarDocumento(
        Authentication authentication,
        @Valid @RequestBody ClienteDocumentoRequest request
    ) {
        return clientePerfilService.agregarDocumento(authentication.getName(), request);
    }

    @PatchMapping("/documentos/{idDocumento}/principal")
    public ClienteDocumentoResponse marcarDocumentoPrincipal(
        Authentication authentication,
        @PathVariable Long idDocumento
    ) {
        return clientePerfilService.marcarDocumentoPrincipal(authentication.getName(), idDocumento);
    }

    @DeleteMapping("/documentos/{idDocumento}")
    public void eliminarDocumento(
        Authentication authentication,
        @PathVariable Long idDocumento
    ) {
        clientePerfilService.eliminarDocumento(authentication.getName(), idDocumento);
    }

    @PatchMapping("/documento")
    public ClientePerfilResponse actualizarDocumento(
        Authentication authentication,
        @Valid @RequestBody ActualizarDocumentoRequest request
    ) {
        return clientePerfilService.actualizarDocumento(authentication.getName(), request);
    }

    @PatchMapping("/datos-personales")
    public void actualizarDatosPersonales(
        Authentication authentication,
        @Valid @RequestBody ActualizarDatosPersonalesRequest request
    ) {
        clientePerfilService.actualizarDatosPersonales(authentication.getName(), request);
    }

    @PatchMapping("/password")
    public String cambiarPassword(
        Authentication authentication,
        @Valid @RequestBody CambiarPasswordRequest request
    ) {
        return clientePerfilService.cambiarPassword(authentication.getName(), request);
    }
}
