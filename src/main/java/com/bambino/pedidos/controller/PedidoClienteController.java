package com.bambino.pedidos.controller;

import com.bambino.pedidos.dto.PedidoCrearDesdeCheckoutRequest;
import com.bambino.pedidos.dto.PedidoCambiarEstadoRequest;
import com.bambino.pedidos.dto.PedidoEstadoHistorialResponse;
import com.bambino.pedidos.dto.PedidoResponse;
import com.bambino.pedidos.service.PedidoEstadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente/pedidos")
/**
 * Clase que maneja la funcionalidad de PedidoClienteController.
 */
public class PedidoClienteController {

    private final PedidoEstadoService pedidoEstadoService;

    public PedidoClienteController(PedidoEstadoService pedidoEstadoService) {
        this.pedidoEstadoService = pedidoEstadoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de crearDesdeCheckout.
     * @param authentication parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PedidoResponse crearDesdeCheckout(Authentication authentication, @Valid @RequestBody PedidoCrearDesdeCheckoutRequest request) {
        return pedidoEstadoService.crearPedidoDesdeCheckout(authentication.getName(), request);
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de listarMisPedidos.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<PedidoResponse> listarMisPedidos(Authentication authentication) {
        return pedidoEstadoService.listarPedidosCliente(authentication.getName());
    }

    @GetMapping("/{idPedido}")
    /**
     * Metodo que realiza la operacion de obtenerMiPedido.
     * @param authentication parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PedidoResponse obtenerMiPedido(Authentication authentication, @PathVariable Long idPedido) {
        return pedidoEstadoService.obtenerPedidoCliente(authentication.getName(), idPedido);
    }

    @GetMapping("/{idPedido}/historial")
    /**
     * Metodo que realiza la operacion de historial.
     * @param authentication parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<PedidoEstadoHistorialResponse> historial(Authentication authentication, @PathVariable Long idPedido) {
        return pedidoEstadoService.historialPedidoCliente(authentication.getName(), idPedido);
    }

    @PatchMapping("/{idPedido}/cancelar")
    public PedidoResponse cancelarMiPedido(Authentication authentication,
                                           @PathVariable Long idPedido,
                                           @Valid @RequestBody PedidoCambiarEstadoRequest request) {
        return pedidoEstadoService.cancelarPedidoCliente(authentication.getName(), idPedido, request.motivo());
    }
}
