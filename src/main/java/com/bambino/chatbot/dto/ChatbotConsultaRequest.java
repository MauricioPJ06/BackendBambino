package com.bambino.chatbot.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Record que maneja la funcionalidad de ChatbotConsultaRequest.
 */
public record ChatbotConsultaRequest(
    @NotBlank(message = "la opcion es obligatoria")
    String opcion,
    Long idPedido,
    String codigoPedido
) {}
