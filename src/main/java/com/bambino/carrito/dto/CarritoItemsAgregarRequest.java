package com.bambino.carrito.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CarritoItemsAgregarRequest(
    @NotEmpty(message = "items es obligatorio")
    @Size(max = 20, message = "no puede agregar mas de 20 items a la vez")
    List<@Valid CarritoItemAgregarRequest> items
) {}
