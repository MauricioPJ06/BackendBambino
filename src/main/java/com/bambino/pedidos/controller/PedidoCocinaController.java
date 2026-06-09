package com.bambino.pedidos.controller;

import com.bambino.pedidos.dto.PedidoAsignarRequest;
import com.bambino.pedidos.dto.PedidoAsignacionCocinaResponse;
import com.bambino.pedidos.dto.PedidoCambiarEstadoRequest;
import com.bambino.pedidos.dto.PedidoCocinaIncidenciaRequest;
import com.bambino.pedidos.dto.PedidoCocinaIncidenciaResponse;
import com.bambino.pedidos.dto.PedidoResponse;
import com.bambino.pedidos.service.PedidoEstadoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cocina/pedidos")
/**
 * Clase que maneja la funcionalidad de PedidoCocinaController.
 */
public class PedidoCocinaController {

    private final PedidoEstadoService pedidoEstadoService;

    public PedidoCocinaController(PedidoEstadoService pedidoEstadoService) {
        this.pedidoEstadoService = pedidoEstadoService;
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de listarOperativos.
     * @return resultado de la operacion
     */
    public List<PedidoResponse> listarOperativos() {
        return pedidoEstadoService.listarPedidosCocinaOperativos();
    }

    @PostMapping("/{idPedido}/tomar")
    public PedidoResponse tomarPedido(
        Authentication authentication,
        @PathVariable Long idPedido,
        @Valid @RequestBody PedidoAsignarRequest request
    ) {
        return pedidoEstadoService.tomarPedidoCocina(authentication.getName(), idPedido, request);
    }

    @PatchMapping("/{idPedido}/estado")
    public PedidoResponse cambiarEstado(
        Authentication authentication,
        @PathVariable Long idPedido,
        @Valid @RequestBody PedidoCambiarEstadoRequest request
    ) {
        return pedidoEstadoService.cambiarEstadoCocina(authentication.getName(), idPedido, request);
    }

    @GetMapping("/{idPedido}/asignaciones")
    public List<PedidoAsignacionCocinaResponse> listarAsignaciones(
        Authentication authentication,
        @PathVariable Long idPedido
    ) {
        return pedidoEstadoService.listarAsignacionesCocina(authentication.getName(), idPedido);
    }

    @PostMapping("/{idPedido}/incidencias")
    public PedidoCocinaIncidenciaResponse registrarIncidencia(
        Authentication authentication,
        @PathVariable Long idPedido,
        @Valid @RequestBody PedidoCocinaIncidenciaRequest request
    ) {
        return pedidoEstadoService.registrarIncidenciaCocina(authentication.getName(), idPedido, request);
    }

    @GetMapping("/{idPedido}/incidencias")
    public List<PedidoCocinaIncidenciaResponse> listarIncidencias(
        Authentication authentication,
        @PathVariable Long idPedido
    ) {
        return pedidoEstadoService.listarIncidenciasCocina(authentication.getName(), idPedido);
    }
}
