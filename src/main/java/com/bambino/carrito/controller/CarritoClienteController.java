package com.bambino.carrito.controller;

import com.bambino.carrito.dto.CarritoItemActualizarRequest;
import com.bambino.carrito.dto.CarritoItemAgregarRequest;
import com.bambino.carrito.dto.CarritoItemsAgregarRequest;
import com.bambino.carrito.dto.CarritoResumenResponse;
import com.bambino.carrito.dto.CheckoutValidarRequest;
import com.bambino.carrito.dto.CheckoutValidarResponse;
import com.bambino.carrito.service.CarritoClienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/cliente/carrito")
/**
 * Clase que maneja la funcionalidad de CarritoClienteController.
 */
public class CarritoClienteController {

    private final CarritoClienteService carritoClienteService;

    public CarritoClienteController(CarritoClienteService carritoClienteService) {
        this.carritoClienteService = carritoClienteService;
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de verCarrito.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CarritoResumenResponse verCarrito(Authentication authentication) {
        return carritoClienteService.verCarrito(authentication.getName());
    }

    @PostMapping("/items")
    public CarritoResumenResponse agregarItem(
        Authentication authentication,
        @Valid @RequestBody CarritoItemAgregarRequest request
    ) {
        return carritoClienteService.agregarItem(authentication.getName(), request);
    }

    @PostMapping("/items/bulk")
    public CarritoResumenResponse agregarItems(
        Authentication authentication,
        @Valid @RequestBody CarritoItemsAgregarRequest request
    ) {
        return carritoClienteService.agregarItems(authentication.getName(), request);
    }

    @PutMapping("/items/{idCarritoItem}")
    public CarritoResumenResponse actualizarItem(
        Authentication authentication,
        @PathVariable Long idCarritoItem,
        @Valid @RequestBody CarritoItemActualizarRequest request
    ) {
        return carritoClienteService.actualizarItem(authentication.getName(), idCarritoItem, request);
    }

    @DeleteMapping("/items/{idCarritoItem}")
    /**
     * Metodo que realiza la operacion de quitarItem.
     * @param authentication parametro de entrada para la operacion
     * @param idCarritoItem parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CarritoResumenResponse quitarItem(Authentication authentication, @PathVariable Long idCarritoItem) {
        return carritoClienteService.quitarItem(authentication.getName(), idCarritoItem);
    }

    @DeleteMapping("/items")
    /**
     * Metodo que realiza la operacion de vaciarCarrito.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CarritoResumenResponse vaciarCarrito(Authentication authentication) {
        return carritoClienteService.vaciarCarrito(authentication.getName());
    }

    @PostMapping("/checkout/validar")
    public CheckoutValidarResponse validarCheckout(
        Authentication authentication,
        @Valid @RequestBody CheckoutValidarRequest request
    ) {
        return carritoClienteService.validarCheckout(authentication.getName(), request);
    }

    @PatchMapping("/checkout/confirmar")
    public CheckoutValidarResponse confirmarCheckout(
        Authentication authentication,
        @Valid @RequestBody CheckoutValidarRequest request
    ) {
        return carritoClienteService.confirmarCheckout(authentication.getName(), request);
    }
}
