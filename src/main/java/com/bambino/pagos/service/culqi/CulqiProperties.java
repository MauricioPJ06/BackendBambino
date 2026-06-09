package com.bambino.pagos.service.culqi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CulqiProperties {

    private final String publicKey;
    private final String secretKey;
    private final String apiBaseUrl;
    private final String checkoutScriptUrl;
    private final String currency;
    private final BigDecimal minimumOrderAmount;
    private final String sandboxPhoneNumber;

    public CulqiProperties(String publicKey,
                           String secretKey,
                           String apiBaseUrl,
                           String checkoutScriptUrl,
                           String currency) {
        this(publicKey, secretKey, apiBaseUrl, checkoutScriptUrl, currency, "6.00", "900000001");
    }

    @Autowired
    public CulqiProperties(
        @Value("${culqi.public-key:}") String publicKey,
        @Value("${culqi.secret-key:}") String secretKey,
        @Value("${culqi.api-base-url:https://api.culqi.com}") String apiBaseUrl,
        @Value("${culqi.checkout-script-url:https://js.culqi.com/checkout-js}") String checkoutScriptUrl,
        @Value("${culqi.currency:PEN}") String currency,
        @Value("${culqi.minimum-order-amount:6.00}") String minimumOrderAmount,
        @Value("${culqi.sandbox-phone-number:900000001}") String sandboxPhoneNumber
    ) {
        this.publicKey = limpiar(publicKey);
        this.secretKey = limpiar(secretKey);
        this.apiBaseUrl = limpiar(apiBaseUrl);
        this.checkoutScriptUrl = limpiar(checkoutScriptUrl);
        this.currency = limpiar(currency);
        this.minimumOrderAmount = parseAmount(minimumOrderAmount);
        this.sandboxPhoneNumber = limpiar(sandboxPhoneNumber).isBlank() ? "900000001" : limpiar(sandboxPhoneNumber);
    }

    public String publicKey() {
        return publicKey;
    }

    public String secretKey() {
        return secretKey;
    }

    public String apiBaseUrl() {
        return apiBaseUrl.isBlank() ? "https://api.culqi.com" : apiBaseUrl;
    }

    public String checkoutScriptUrl() {
        return checkoutScriptUrl.isBlank() ? "https://js.culqi.com/checkout-js" : checkoutScriptUrl;
    }

    public String currency() {
        return currency.isBlank() ? "PEN" : currency;
    }

    public BigDecimal minimumOrderAmount() {
        return minimumOrderAmount;
    }

    public String sandboxPhoneNumber() {
        return sandboxPhoneNumber;
    }

    public boolean testMode() {
        return publicKey.startsWith("pk_test_") || secretKey.startsWith("sk_test_");
    }

    public boolean enabled() {
        return isRealKey(publicKey, "pk_") && isRealKey(secretKey, "sk_");
    }

    private String limpiar(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isRealKey(String value, String prefix) {
        return value.startsWith(prefix) && !value.endsWith("_xxx");
    }

    private BigDecimal parseAmount(String value) {
        try {
            return new BigDecimal(limpiar(value).isBlank() ? "6.00" : limpiar(value));
        } catch (NumberFormatException e) {
            return new BigDecimal("6.00");
        }
    }
}
