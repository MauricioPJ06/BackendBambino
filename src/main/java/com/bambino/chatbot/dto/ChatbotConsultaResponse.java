package com.bambino.chatbot.dto;

import java.util.Map;

/**
 * Record que maneja la funcionalidad de ChatbotConsultaResponse.
 */
public record ChatbotConsultaResponse(
    String opcion,
    String mensaje,
    Map<String, Object> datos
) {}
