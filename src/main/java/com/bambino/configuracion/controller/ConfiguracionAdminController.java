package com.bambino.configuracion.controller;

import com.bambino.configuracion.dto.*;
import com.bambino.configuracion.service.ConfiguracionAdminService;
import com.bambino.configuracion.service.ConfiguracionMediaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/configuracion")
/**
 * Clase que maneja la funcionalidad de ConfiguracionAdminController.
 */
public class ConfiguracionAdminController {

    private final ConfiguracionAdminService configuracionAdminService;
    private final ConfiguracionMediaService configuracionMediaService;

    public ConfiguracionAdminController(ConfiguracionAdminService configuracionAdminService,
                                        ConfiguracionMediaService configuracionMediaService) {
        this.configuracionAdminService = configuracionAdminService;
        this.configuracionMediaService = configuracionMediaService;
    }

    @GetMapping("/global")
    /**
     * Metodo que realiza la operacion de obtenerGlobal.
     * @return resultado de la operacion
     */
    public ConfiguracionGlobalResponse obtenerGlobal() {
        return configuracionAdminService.obtenerConfiguracionGlobal();
    }

    @PutMapping("/global")
    /**
     * Metodo que realiza la operacion de actualizarGlobal.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ConfiguracionGlobalResponse actualizarGlobal(@Valid @RequestBody ConfiguracionGlobalRequest request) {
        return configuracionAdminService.actualizarConfiguracionGlobal(request);
    }

    @GetMapping("/zonas-delivery")
    /**
     * Metodo que realiza la operacion de listarZonas.
     * @return resultado de la operacion
     */
    public List<ZonaDeliveryResponse> listarZonas() {
        return configuracionAdminService.listarZonas();
    }

    @PostMapping("/zonas-delivery")
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de crearZona.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ZonaDeliveryResponse crearZona(@Valid @RequestBody ZonaDeliveryRequest request) {
        return configuracionAdminService.crearZona(request);
    }

    @PutMapping("/zonas-delivery/{idZona}")
    public ZonaDeliveryResponse actualizarZona(@PathVariable Long idZona,
                                               @Valid @RequestBody ZonaDeliveryRequest request) {
        return configuracionAdminService.actualizarZona(idZona, request);
    }

    @GetMapping("/empresas")
    /**
     * Metodo que realiza la operacion de listarEmpresas.
     * @return resultado de la operacion
     */
    public List<EmpresaResponse> listarEmpresas() {
        return configuracionAdminService.listarEmpresas();
    }

    @PostMapping("/empresas")
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de crearEmpresa.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public EmpresaResponse crearEmpresa(@Valid @RequestBody EmpresaRequest request) {
        return configuracionAdminService.crearEmpresa(request);
    }

    @PutMapping("/empresas/{idEmpresa}")
    public EmpresaResponse actualizarEmpresa(@PathVariable Long idEmpresa,
                                             @Valid @RequestBody EmpresaRequest request) {
        return configuracionAdminService.actualizarEmpresa(idEmpresa, request);
    }

    @GetMapping("/series-comprobante")
    /**
     * Metodo que realiza la operacion de listarSeries.
     * @return resultado de la operacion
     */
    public List<SerieComprobanteResponse> listarSeries() {
        return configuracionAdminService.listarSeries();
    }

    @PostMapping("/series-comprobante")
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de crearSerie.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public SerieComprobanteResponse crearSerie(@Valid @RequestBody SerieComprobanteRequest request) {
        return configuracionAdminService.crearSerie(request);
    }

    @PutMapping("/series-comprobante/{idSerie}")
    public SerieComprobanteResponse actualizarSerie(@PathVariable Long idSerie,
                                                    @Valid @RequestBody SerieComprobanteRequest request) {
        return configuracionAdminService.actualizarSerie(idSerie, request);
    }

    @GetMapping("/transiciones-pedido")
    /**
     * Metodo que realiza la operacion de listarTransicionesPedido.
     * @return resultado de la operacion
     */
    public List<TransicionPedidoConfigResponse> listarTransicionesPedido() {
        return configuracionAdminService.listarTransicionesPedido();
    }

    @PatchMapping("/transiciones-pedido/{idTransicion}")
    public TransicionPedidoConfigResponse actualizarTransicionPedido(@PathVariable Long idTransicion,
                                                                     @Valid @RequestBody TransicionPedidoConfigRequest request) {
        return configuracionAdminService.actualizarTransicionPedido(idTransicion, request);
    }

    @GetMapping("/media")
    /**
     * Metodo que realiza la operacion de listarMediaAdmin.
     * @return resultado de la operacion
     */
    public List<ConfiguracionMediaResponse> listarMediaAdmin() {
        return configuracionMediaService.listarAdmin();
    }

    @GetMapping("/media/{clave}")
    /**
     * Metodo que realiza la operacion de obtenerMediaAdmin.
     * @param clave parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ConfiguracionMediaResponse obtenerMediaAdmin(@PathVariable String clave) {
        return configuracionMediaService.obtenerAdminPorClave(clave);
    }

    @PutMapping("/media/{clave}")
    public ConfiguracionMediaResponse actualizarMedia(@PathVariable String clave,
                                                      @Valid @RequestBody ConfiguracionMediaRequest request) {
        return configuracionMediaService.actualizarPorClave(clave, request);
    }

    @PutMapping("/media/{clave}/archivo")
    public ConfiguracionMediaResponse subirArchivoMedia(@PathVariable String clave,
                                                        @RequestParam("archivo") MultipartFile archivo) {
        return configuracionMediaService.subirArchivoPorClave(clave, archivo);
    }
}
