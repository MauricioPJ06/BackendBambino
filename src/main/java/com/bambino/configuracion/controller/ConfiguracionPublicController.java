package com.bambino.configuracion.controller;

import com.bambino.configuracion.dto.ConfiguracionMediaResponse;
import com.bambino.configuracion.dto.EmpresaResponse;
import com.bambino.configuracion.service.ConfiguracionAdminService;
import com.bambino.configuracion.service.ConfiguracionMediaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/configuracion")
/**
 * Clase que maneja la funcionalidad de ConfiguracionPublicController.
 */
public class ConfiguracionPublicController {

    private final ConfiguracionMediaService configuracionMediaService;
    private final ConfiguracionAdminService configuracionAdminService;

    public ConfiguracionPublicController(ConfiguracionMediaService configuracionMediaService,
                                         ConfiguracionAdminService configuracionAdminService) {
        this.configuracionMediaService = configuracionMediaService;
        this.configuracionAdminService = configuracionAdminService;
    }

    @GetMapping("/media")
    /**
     * Metodo que realiza la operacion de listarMediaActiva.
     * @return resultado de la operacion
     */
    public List<ConfiguracionMediaResponse> listarMediaActiva() {
        return configuracionMediaService.listarPublicasActivas();
    }

    @GetMapping("/media/{clave}")
    /**
     * Metodo que realiza la operacion de obtenerMediaActivaPorClave.
     * @param clave parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ConfiguracionMediaResponse obtenerMediaActivaPorClave(@PathVariable String clave) {
        return configuracionMediaService.obtenerPublicaPorClave(clave);
    }

    @GetMapping("/empresas")
    /**
     * Metodo que realiza la operacion de listarEmpresasPublicas.
     * @return resultado de la operacion
     */
    public List<EmpresaResponse> listarEmpresasPublicas() {
        return configuracionAdminService.listarEmpresas();
    }
}
