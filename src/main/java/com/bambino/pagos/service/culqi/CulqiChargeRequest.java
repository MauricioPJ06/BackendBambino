package com.bambino.pagos.service.culqi;

import java.math.BigDecimal;

public record CulqiChargeRequest(
    BigDecimal amount,
    String currency,
    String email,
    String sourceId,
    String description,
    String orderId,
    String documentNumber
) {}
