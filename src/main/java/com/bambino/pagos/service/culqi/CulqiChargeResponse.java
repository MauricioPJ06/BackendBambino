package com.bambino.pagos.service.culqi;

public record CulqiChargeResponse(
    String id,
    String object,
    String userMessage,
    String actionCode
) {}
