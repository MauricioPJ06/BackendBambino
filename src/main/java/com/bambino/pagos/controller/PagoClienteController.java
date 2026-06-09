package com.bambino.pagos.controller;

import com.bambino.carrito.dto.CheckoutValidarRequest;
import com.bambino.pagos.dto.PagoIniciarRequest;
import com.bambino.pagos.dto.PagoCheckoutConfirmarRequest;
import com.bambino.pagos.dto.PagoCheckoutOrdenResponse;
import com.bambino.pagos.dto.PagoCheckoutResponse;
import com.bambino.pagos.dto.PagoResponse;
import com.bambino.pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente/pagos")
/**
 * Clase que maneja la funcionalidad de PagoClienteController.
 */
public class PagoClienteController {

    private final PagoService pagoService;

    public PagoClienteController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping("/iniciar")
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de iniciar.
     * @param authentication parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PagoResponse iniciar(Authentication authentication, @Valid @RequestBody PagoIniciarRequest request) {
        return pagoService.iniciarPagoCliente(authentication.getName(), request);
    }

    @PostMapping("/checkout/confirmar")
    @ResponseStatus(HttpStatus.CREATED)
    public PagoCheckoutResponse confirmarCheckout(Authentication authentication, @Valid @RequestBody PagoCheckoutConfirmarRequest request) {
        return pagoService.confirmarPagoCheckoutCliente(authentication.getName(), request);
    }

    @PostMapping("/checkout/culqi/orden")
    @ResponseStatus(HttpStatus.CREATED)
    public PagoCheckoutOrdenResponse crearOrdenCulqiCheckout(Authentication authentication, @Valid @RequestBody CheckoutValidarRequest request) {
        return pagoService.crearOrdenCulqiCheckoutCliente(authentication.getName(), request);
    }

    @GetMapping("/pedido/{idPedido}")
    /**
     * Metodo que realiza la operacion de listarPorPedido.
     * @param authentication parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<PagoResponse> listarPorPedido(Authentication authentication, @PathVariable Long idPedido) {
        return pagoService.listarPagosClientePorPedido(authentication.getName(), idPedido);
    }
}
