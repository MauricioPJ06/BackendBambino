package com.bambino.pagos.service.culqi;

import java.math.BigDecimal;

public record CulqiOrderRequest(
    BigDecimal amount,
    String currency,
    String email,
    String firstName,
    String lastName,
    String phoneNumber,
    String orderNumber,
    String description,
    String documentNumber
) {}
