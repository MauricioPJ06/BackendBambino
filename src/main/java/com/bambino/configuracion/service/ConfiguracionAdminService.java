package com.bambino.configuracion.service;

import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.carrito.entity.ConfiguracionGlobalEntity;
import com.bambino.carrito.entity.ZonaDeliveryEntity;
import com.bambino.carrito.repository.ConfiguracionGlobalRepository;
import com.bambino.carrito.repository.ZonaDeliveryRepository;
import com.bambino.comprobantes.entity.EmpresaEntity;
import com.bambino.comprobantes.entity.SerieComprobanteEntity;
import com.bambino.comprobantes.entity.TipoComprobante;
import com.bambino.comprobantes.repository.EmpresaRepository;
import com.bambino.comprobantes.repository.SerieComprobanteRepository;
import com.bambino.configuracion.dto.*;
import com.bambino.pedidos.entity.PedidoEstadoTransicionPermitidaEntity;
import com.bambino.pedidos.repository.PedidoEstadoTransicionPermitidaRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@Service
/**
 * Clase que maneja la funcionalidad de ConfiguracionAdminService.
 */
public class ConfiguracionAdminService {

    private final ConfiguracionGlobalRepository configuracionGlobalRepository;
    private final ZonaDeliveryRepository zonaDeliveryRepository;
    private final EmpresaRepository empresaRepository;
    private final SerieComprobanteRepository serieComprobanteRepository;
    private final PedidoEstadoTransicionPermitidaRepository transicionPermitidaRepository;
    private final AuditoriaService auditoriaService;

    public ConfiguracionAdminService(ConfiguracionGlobalRepository configuracionGlobalRepository,
                                     ZonaDeliveryRepository zonaDeliveryRepository,
                                     EmpresaRepository empresaRepository,
                                     SerieComprobanteRepository serieComprobanteRepository,
                                     PedidoEstadoTransicionPermitidaRepository transicionPermitidaRepository,
                                     AuditoriaService auditoriaService) {
        this.configuracionGlobalRepository = configuracionGlobalRepository;
        this.zonaDeliveryRepository = zonaDeliveryRepository;
        this.empresaRepository = empresaRepository;
        this.serieComprobanteRepository = serieComprobanteRepository;
        this.transicionPermitidaRepository = transicionPermitidaRepository;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerConfiguracionGlobal.
     * @return resultado de la operacion
     */
    public ConfiguracionGlobalResponse obtenerConfiguracionGlobal() {
        ConfiguracionGlobalEntity c = configuracionGlobalRepository.findById((short) 1)
            .orElseThrow(() -> new NegocioException("no existe configuracion_global id_config=1"));
        return toResponse(c);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarConfiguracionGlobal.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ConfiguracionGlobalResponse actualizarConfiguracionGlobal(ConfiguracionGlobalRequest request) {
        if (request.deliveryTiempoMaxMinutos() < request.deliveryTiempoMinMinutos()) {
            throw new NegocioException("deliveryTiempoMaxMinutos no puede ser menor a deliveryTiempoMinMinutos");
        }

        ConfiguracionGlobalEntity c = configuracionGlobalRepository.findById((short) 1)
            .orElseGet(() -> {
                ConfiguracionGlobalEntity nuevo = new ConfiguracionGlobalEntity();
                nuevo.setIdConfig((short) 1);
                nuevo.setFechaCreacion(LocalDateTime.now());
                return nuevo;
            });

        c.setMoneda(request.moneda().trim().toUpperCase(Locale.ROOT));
        c.setIgvPorcentaje(request.igvPorcentaje());
        c.setDeliveryMontoMinimo(request.deliveryMontoMinimo());
        c.setDeliveryTiempoMinMinutos(request.deliveryTiempoMinMinutos());
        c.setDeliveryTiempoMaxMinutos(request.deliveryTiempoMaxMinutos());
        c.setTimezone(request.timezone().trim());
        c.setFechaActualizacion(LocalDateTime.now());

        ConfiguracionGlobalEntity guardado = configuracionGlobalRepository.save(c);
        auditoriaService.registrar(
            "CONFIGURACION_GLOBAL",
            String.valueOf(guardado.getIdConfig()),
            "ACTUALIZAR_CONFIGURACION_GLOBAL",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"moneda\":\"" + guardado.getMoneda() + "\"}"
        );
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarZonas.
     * @return resultado de la operacion
     */
    public List<ZonaDeliveryResponse> listarZonas() {
        return zonaDeliveryRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearZona.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ZonaDeliveryResponse crearZona(ZonaDeliveryRequest request) {
        ZonaDeliveryEntity z = new ZonaDeliveryEntity();
        aplicarZona(z, request);
        z.setFechaCreacion(LocalDateTime.now());
        z.setFechaActualizacion(LocalDateTime.now());
        ZonaDeliveryEntity guardada = zonaDeliveryRepository.save(z);
        auditoriaService.registrar(
            "ZONA_DELIVERY",
            String.valueOf(guardada.getIdZona()),
            "CREAR_ZONA_DELIVERY",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"nombre\":\"" + guardada.getNombre() + "\"}"
        );
        return toResponse(guardada);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarZona.
     * @param idZona parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ZonaDeliveryResponse actualizarZona(Long idZona, ZonaDeliveryRequest request) {
        ZonaDeliveryEntity z = zonaDeliveryRepository.findById(idZona)
            .orElseThrow(() -> new NegocioException("zona_delivery no existe"));
        if (!zonaTieneCambios(z, request)) {
            throw new NegocioException("no hay cambios para guardar en ubicacion");
        }
        aplicarZona(z, request);
        z.setFechaActualizacion(LocalDateTime.now());
        ZonaDeliveryEntity guardada = zonaDeliveryRepository.save(z);
        auditoriaService.registrar(
            "ZONA_DELIVERY",
            String.valueOf(guardada.getIdZona()),
            "ACTUALIZAR_ZONA_DELIVERY",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"nombre\":\"" + guardada.getNombre() + "\"}"
        );
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarEmpresas.
     * @return resultado de la operacion
     */
    public List<EmpresaResponse> listarEmpresas() {
        return empresaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearEmpresa.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public EmpresaResponse crearEmpresa(EmpresaRequest request) {
        if (empresaRepository.count() > 0) {
            throw new NegocioException("solo se permite una empresa emisora principal");
        }
        EmpresaEntity e = new EmpresaEntity();
        aplicarEmpresa(e, request);
        e.setFechaCreacion(LocalDateTime.now());
        e.setFechaActualizacion(LocalDateTime.now());
        EmpresaEntity guardada = empresaRepository.save(e);
        auditoriaService.registrar(
            "EMPRESA",
            String.valueOf(guardada.getIdEmpresa()),
            "CREAR_EMPRESA",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"ruc\":\"" + guardada.getRuc() + "\"}"
        );
        return toResponse(guardada);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarEmpresa.
     * @param idEmpresa parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public EmpresaResponse actualizarEmpresa(Long idEmpresa, EmpresaRequest request) {
        EmpresaEntity e = empresaRepository.findById(idEmpresa)
            .orElseThrow(() -> new NegocioException("empresa no existe"));
        if (!empresaTieneCambios(e, request)) {
            throw new NegocioException("no hay cambios para guardar en empresa");
        }
        aplicarEmpresa(e, request);
        e.setFechaActualizacion(LocalDateTime.now());
        EmpresaEntity guardada = empresaRepository.save(e);
        auditoriaService.registrar(
            "EMPRESA",
            String.valueOf(guardada.getIdEmpresa()),
            "ACTUALIZAR_EMPRESA",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"ruc\":\"" + guardada.getRuc() + "\"}"
        );
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarSeries.
     * @return resultado de la operacion
     */
    public List<SerieComprobanteResponse> listarSeries() {
        return serieComprobanteRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearSerie.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public SerieComprobanteResponse crearSerie(SerieComprobanteRequest request) {
        empresaRepository.findById(request.idEmpresa())
            .orElseThrow(() -> new NegocioException("empresa no existe para crear serie"));

        SerieComprobanteEntity s = new SerieComprobanteEntity();
        aplicarSerie(s, request);
        s.setFechaCreacion(LocalDateTime.now());
        s.setFechaActualizacion(LocalDateTime.now());
        SerieComprobanteEntity guardada = serieComprobanteRepository.save(s);
        auditoriaService.registrar(
            "SERIE_COMPROBANTE",
            String.valueOf(guardada.getIdSerie()),
            "CREAR_SERIE_COMPROBANTE",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"serie\":\"" + guardada.getSerie() + "\",\"tipo\":\"" + guardada.getTipoComprobante().name() + "\"}"
        );
        return toResponse(guardada);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarSerie.
     * @param idSerie parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public SerieComprobanteResponse actualizarSerie(Long idSerie, SerieComprobanteRequest request) {
        empresaRepository.findById(request.idEmpresa())
            .orElseThrow(() -> new NegocioException("empresa no existe para actualizar serie"));

        SerieComprobanteEntity s = serieComprobanteRepository.findById(idSerie)
            .orElseThrow(() -> new NegocioException("serie_comprobante no existe"));
        aplicarSerie(s, request);
        s.setFechaActualizacion(LocalDateTime.now());
        SerieComprobanteEntity guardada = serieComprobanteRepository.save(s);
        auditoriaService.registrar(
            "SERIE_COMPROBANTE",
            String.valueOf(guardada.getIdSerie()),
            "ACTUALIZAR_SERIE_COMPROBANTE",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"serie\":\"" + guardada.getSerie() + "\",\"tipo\":\"" + guardada.getTipoComprobante().name() + "\"}"
        );
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarTransicionesPedido.
     * @return resultado de la operacion
     */
    public List<TransicionPedidoConfigResponse> listarTransicionesPedido() {
        return transicionPermitidaRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarTransicionPedido.
     * @param idTransicion parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public TransicionPedidoConfigResponse actualizarTransicionPedido(Long idTransicion, TransicionPedidoConfigRequest request) {
        PedidoEstadoTransicionPermitidaEntity t = transicionPermitidaRepository.findById(idTransicion)
            .orElseThrow(() -> new NegocioException("transicion de pedido no existe"));
        t.setActivo(request.activo());
        PedidoEstadoTransicionPermitidaEntity guardada = transicionPermitidaRepository.save(t);
        auditoriaService.registrar(
            "PEDIDO_TRANSICION",
            String.valueOf(guardada.getIdTransicion()),
            "ACTUALIZAR_TRANSICION_PEDIDO",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"activo\":" + guardada.getActivo() + "}"
        );
        return toResponse(guardada);
    }

    private void aplicarZona(ZonaDeliveryEntity z, ZonaDeliveryRequest request) {
        z.setNombre(request.nombre().trim());
        z.setActivo(request.activo());
        z.setTarifaBase(request.tarifaBase());
        z.setMontoMinimo(request.montoMinimo());
        z.setTiempoEstimadoMinutos(request.tiempoEstimadoMinutos());
        z.setCoberturaDescripcion(limpiar(request.coberturaDescripcion()));
        z.setMapaEmbedUrl(limpiar(request.mapaEmbedUrl()));
        validarCoordenadasZona(request.latitudCentro(), request.longitudCentro(), request.radioKm());
        z.setLatitudCentro(request.latitudCentro());
        z.setLongitudCentro(request.longitudCentro());
        z.setRadioKm(request.radioKm());
        z.setHoraInicioAtencion(parseHora(request.horaInicioAtencion()));
        z.setHoraFinAtencion(parseHora(request.horaFinAtencion()));
    }

    private void aplicarEmpresa(EmpresaEntity e, EmpresaRequest request) {
        e.setRuc(request.ruc().trim());
        e.setRazonSocial(request.razonSocial().trim());
        e.setNombreComercial(limpiar(request.nombreComercial()));
        e.setDireccionFiscal(request.direccionFiscal().trim());
        e.setTelefono(limpiar(request.telefono()));
        e.setCorreo(limpiar(request.correo()));
        e.setActivo(request.activo());
    }

    private boolean empresaTieneCambios(EmpresaEntity e, EmpresaRequest request) {
        return !strEq(e.getRuc(), request.ruc())
            || !strEq(e.getRazonSocial(), request.razonSocial())
            || !strEqNull(e.getNombreComercial(), request.nombreComercial())
            || !strEq(e.getDireccionFiscal(), request.direccionFiscal())
            || !strEqNull(e.getTelefono(), request.telefono())
            || !strEqNull(e.getCorreo(), request.correo())
            || !boolEq(e.getActivo(), request.activo());
    }

    private boolean zonaTieneCambios(ZonaDeliveryEntity z, ZonaDeliveryRequest request) {
        return !strEq(z.getNombre(), request.nombre())
            || !boolEq(z.getActivo(), request.activo())
            || !numEq(z.getTarifaBase(), request.tarifaBase())
            || !numEq(z.getMontoMinimo(), request.montoMinimo())
            || !intEq(z.getTiempoEstimadoMinutos(), request.tiempoEstimadoMinutos())
            || !strEqNull(z.getCoberturaDescripcion(), request.coberturaDescripcion())
            || !strEqNull(z.getMapaEmbedUrl(), request.mapaEmbedUrl())
            || !numEq(z.getLatitudCentro(), request.latitudCentro())
            || !numEq(z.getLongitudCentro(), request.longitudCentro())
            || !numEq(z.getRadioKm(), request.radioKm())
            || !timeEq(z.getHoraInicioAtencion(), parseHora(request.horaInicioAtencion()))
            || !timeEq(z.getHoraFinAtencion(), parseHora(request.horaFinAtencion()));
    }

    private void aplicarSerie(SerieComprobanteEntity s, SerieComprobanteRequest request) {
        s.setIdEmpresa(request.idEmpresa());
        s.setTipoComprobante(parseTipoComprobante(request.tipoComprobante()));
        s.setSerie(request.serie().trim().toUpperCase(Locale.ROOT));
        s.setCorrelativoActual(request.correlativoActual());
        s.setActivo(request.activo());
    }

    private TipoComprobante parseTipoComprobante(String valor) {
        try {
            return TipoComprobante.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new NegocioException("tipoComprobante invalido");
        }
    }

    private LocalTime parseHora(String hora) {
        if (hora == null || hora.isBlank()) {
            return null;
        }
        try {
            return LocalTime.parse(hora.trim());
        } catch (Exception e) {
            throw new NegocioException("hora invalida, use formato HH:mm o HH:mm:ss");
        }
    }

    private String limpiar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private boolean strEq(String left, String right) {
        String l = left == null ? "" : left.trim();
        String r = right == null ? "" : right.trim();
        return l.equals(r);
    }

    private boolean strEqNull(String left, String right) {
        return java.util.Objects.equals(limpiar(left), limpiar(right));
    }

    private boolean boolEq(Boolean left, Boolean right) {
        return java.util.Objects.equals(left, right);
    }

    private boolean intEq(Integer left, Integer right) {
        return java.util.Objects.equals(left, right);
    }

    private boolean numEq(java.math.BigDecimal left, java.math.BigDecimal right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        return left.compareTo(right) == 0;
    }

    private boolean timeEq(LocalTime left, LocalTime right) {
        return java.util.Objects.equals(left, right);
    }

    private void validarCoordenadasZona(java.math.BigDecimal latitudCentro,
                                        java.math.BigDecimal longitudCentro,
                                        java.math.BigDecimal radioKm) {
        boolean envioUbicacion = latitudCentro != null || longitudCentro != null || radioKm != null;
        if (!envioUbicacion) {
            return;
        }
        if (latitudCentro == null || longitudCentro == null || radioKm == null) {
            throw new NegocioException("zona delivery requiere latitudCentro, longitudCentro y radioKm completos");
        }
        if (latitudCentro.compareTo(java.math.BigDecimal.valueOf(-90)) < 0 || latitudCentro.compareTo(java.math.BigDecimal.valueOf(90)) > 0) {
            throw new NegocioException("latitudCentro fuera de rango");
        }
        if (longitudCentro.compareTo(java.math.BigDecimal.valueOf(-180)) < 0 || longitudCentro.compareTo(java.math.BigDecimal.valueOf(180)) > 0) {
            throw new NegocioException("longitudCentro fuera de rango");
        }
    }

    private ConfiguracionGlobalResponse toResponse(ConfiguracionGlobalEntity c) {
        return new ConfiguracionGlobalResponse(
            c.getIdConfig(),
            c.getMoneda(),
            c.getIgvPorcentaje(),
            c.getDeliveryMontoMinimo(),
            c.getDeliveryTiempoMinMinutos(),
            c.getDeliveryTiempoMaxMinutos(),
            c.getTimezone()
        );
    }

    private ZonaDeliveryResponse toResponse(ZonaDeliveryEntity z) {
        return new ZonaDeliveryResponse(
            z.getIdZona(),
            z.getNombre(),
            z.getActivo(),
            z.getTarifaBase(),
            z.getMontoMinimo(),
            z.getTiempoEstimadoMinutos(),
            z.getCoberturaDescripcion(),
            z.getMapaEmbedUrl(),
            z.getLatitudCentro(),
            z.getLongitudCentro(),
            z.getRadioKm(),
            z.getHoraInicioAtencion(),
            z.getHoraFinAtencion()
        );
    }

    private EmpresaResponse toResponse(EmpresaEntity e) {
        return new EmpresaResponse(
            e.getIdEmpresa(),
            e.getRuc(),
            e.getRazonSocial(),
            e.getNombreComercial(),
            e.getDireccionFiscal(),
            e.getTelefono(),
            e.getCorreo(),
            e.getActivo()
        );
    }

    private SerieComprobanteResponse toResponse(SerieComprobanteEntity s) {
        return new SerieComprobanteResponse(
            s.getIdSerie(),
            s.getIdEmpresa(),
            s.getTipoComprobante().name(),
            s.getSerie(),
            s.getCorrelativoActual(),
            s.getActivo()
        );
    }

    private TransicionPedidoConfigResponse toResponse(PedidoEstadoTransicionPermitidaEntity t) {
        return new TransicionPedidoConfigResponse(
            t.getIdTransicion(),
            t.getEstadoOrigen().name(),
            t.getEstadoDestino().name(),
            t.getActorTipo().name(),
            t.getActivo()
        );
    }
}
