package com.bambino.pagos.controller;

import com.bambino.pagos.dto.CulqiPublicConfigResponse;
import com.bambino.pagos.service.culqi.CulqiProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/pagos")
public class PagoPublicController {

    private final CulqiProperties culqiProperties;

    public PagoPublicController(CulqiProperties culqiProperties) {
        this.culqiProperties = culqiProperties;
    }

    @GetMapping("/culqi/configuracion")
    public CulqiPublicConfigResponse obtenerConfiguracionCulqi() {
        return new CulqiPublicConfigResponse(
            culqiProperties.publicKey(),
            culqiProperties.checkoutScriptUrl(),
            culqiProperties.currency(),
            culqiProperties.enabled()
        );
    }
}
