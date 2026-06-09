package com.bambino.comprobantes.service;

import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.comprobantes.dto.ComprobanteDetalleResponse;
import com.bambino.comprobantes.dto.ComprobanteResponse;
import com.bambino.comprobantes.entity.*;
import com.bambino.comprobantes.repository.ComprobanteDetalleRepository;
import com.bambino.comprobantes.repository.ComprobanteRepository;
import com.bambino.comprobantes.repository.EmpresaRepository;
import com.bambino.comprobantes.repository.SerieComprobanteRepository;
import com.bambino.pedidos.entity.PedidoEntity;
import com.bambino.pedidos.entity.PedidoItemEntity;
import com.bambino.pedidos.entity.TipoComprobantePedido;
import com.bambino.pedidos.repository.PedidoItemRepository;
import com.bambino.pedidos.repository.PedidoRepository;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
/**
 * Clase que maneja la funcionalidad de ComprobanteService.
 */
public class ComprobanteService {

    private final ComprobanteRepository comprobanteRepository;
    private final ComprobanteDetalleRepository comprobanteDetalleRepository;
    private final SerieComprobanteRepository serieComprobanteRepository;
    private final EmpresaRepository empresaRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComprobanteCorreoService comprobanteCorreoService;
    private final AuditoriaService auditoriaService;

    public ComprobanteService(ComprobanteRepository comprobanteRepository,
                              ComprobanteDetalleRepository comprobanteDetalleRepository,
                              SerieComprobanteRepository serieComprobanteRepository,
                              EmpresaRepository empresaRepository,
                              PedidoRepository pedidoRepository,
                              PedidoItemRepository pedidoItemRepository,
                              UsuarioRepository usuarioRepository,
                              ComprobanteCorreoService comprobanteCorreoService,
                              AuditoriaService auditoriaService) {
        this.comprobanteRepository = comprobanteRepository;
        this.comprobanteDetalleRepository = comprobanteDetalleRepository;
        this.serieComprobanteRepository = serieComprobanteRepository;
        this.empresaRepository = empresaRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.usuarioRepository = usuarioRepository;
        this.comprobanteCorreoService = comprobanteCorreoService;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de emitirSiNoExiste.
     * @param idPedido parametro de entrada para la operacion
     * @param usuarioActor parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ComprobanteResponse emitirSiNoExiste(Long idPedido, Long usuarioActor) {
        ComprobanteEntity existente = comprobanteRepository.findByIdPedido(idPedido).orElse(null);
        if (existente != null) {
            return toResponse(existente);
        }

        PedidoEntity pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado para emitir comprobante"));

        EmpresaEntity empresa = empresaRepository.findFirstByActivoTrueOrderByIdEmpresaAsc()
            .orElseThrow(() -> new NegocioException("no existe empresa activa para emision"));

        TipoComprobante tipo = pedido.getTipoComprobante() == TipoComprobantePedido.FACTURA
            ? TipoComprobante.FACTURA
            : TipoComprobante.BOLETA;

        SerieComprobanteEntity serie = serieComprobanteRepository
            .findFirstByIdEmpresaAndTipoComprobanteAndActivoTrueOrderByIdSerieAsc(empresa.getIdEmpresa(), tipo)
            .orElseThrow(() -> new NegocioException("no existe serie activa para el tipo de comprobante"));

        Long nuevoCorrelativo = serie.getCorrelativoActual() + 1;
        serie.setCorrelativoActual(nuevoCorrelativo);
        serie.setFechaActualizacion(LocalDateTime.now());
        serieComprobanteRepository.save(serie);

        ComprobanteEntity comprobante = new ComprobanteEntity();
        comprobante.setIdEmpresa(empresa.getIdEmpresa());
        comprobante.setIdPedido(pedido.getIdPedido());
        comprobante.setTipo(tipo);
        comprobante.setSerie(serie.getSerie());
        comprobante.setCorrelativo(nuevoCorrelativo);
        comprobante.setNumeroCompleto(formatearNumeroCompleto(serie.getSerie(), nuevoCorrelativo));
        comprobante.setEstado(EstadoComprobante.EMITIDO);
        comprobante.setDocReceptorTipo(parseTipoDocReceptor(pedido.getDocReceptorTipo().name()));
        comprobante.setDocReceptorNumero(pedido.getDocReceptorNumero());
        comprobante.setRazonSocialReceptor(pedido.getRazonSocialSnapshot());
        comprobante.setDireccionFiscalReceptor(pedido.getDireccionFiscalSnapshot());
        comprobante.setSubtotal(pedido.getSubtotal());
        comprobante.setImpuestoTotal(pedido.getImpuestoTotal());
        comprobante.setTotal(pedido.getTotal());
        comprobante.setFechaEmision(LocalDateTime.now());
        comprobante.setUsuarioCreacion(usuarioActor);
        comprobante.setUsuarioActualizacion(usuarioActor);

        ComprobanteEntity guardado = comprobanteRepository.save(comprobante);

        List<PedidoItemEntity> pedidoItems = pedidoItemRepository.findByIdPedidoOrderByIdPedidoItemAsc(pedido.getIdPedido());
        if (pedidoItems.isEmpty()) {
            throw new NegocioException("no existen pedido_item para emitir comprobante");
        }
        for (PedidoItemEntity item : pedidoItems) {
            ComprobanteDetalleEntity detalle = new ComprobanteDetalleEntity();
            detalle.setIdComprobante(guardado.getIdComprobante());
            detalle.setIdPedidoItem(item.getIdPedidoItem());
            detalle.setDescripcionItem(item.getNombreProductoSnapshot());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.setDescuentoUnitario(item.getDescuentoUnitario());
            detalle.setSubtotalLinea(item.getSubtotalLinea());
            detalle.setFechaCreacion(LocalDateTime.now());
            comprobanteDetalleRepository.save(detalle);
        }
        auditoriaService.registrar(
            "COMPROBANTE",
            String.valueOf(guardado.getIdComprobante()),
            "EMITIR_COMPROBANTE",
            AuditoriaActorTipo.ADMIN,
            usuarioActor,
            AuditoriaCanal.WEB,
            "{\"idPedido\":" + guardado.getIdPedido() + ",\"numero\":\"" + guardado.getNumeroCompleto() + "\"}"
        );

        return toResponse(guardado);
    }

    private TipoDocumentoReceptor parseTipoDocReceptor(String valor) {
        try {
            return TipoDocumentoReceptor.valueOf(valor);
        } catch (Exception e) {
            return TipoDocumentoReceptor.OTRO;
        }
    }

    private String formatearNumeroCompleto(String serie, Long correlativo) {
        return serie + "-" + String.format("%09d", correlativo);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerPorPedidoCliente.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ComprobanteResponse obtenerPorPedidoCliente(String emailAutenticado, Long idPedido) {
        Long idCliente = obtenerIdUsuario(emailAutenticado);
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));
        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new NegocioException("el pedido no pertenece al cliente autenticado");
        }

        ComprobanteEntity comprobante = comprobanteRepository.findByIdPedido(idPedido)
            .orElseThrow(() -> new NegocioException("el pedido aun no tiene comprobante emitido"));
        return toResponse(comprobante);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarAdmin.
     * @return resultado de la operacion
     */
    public List<ComprobanteResponse> listarAdmin() {
        return comprobanteRepository.findAllByOrderByFechaEmisionDesc().stream().map(this::toResponse).toList();
    }

    @Transactional
    public ComprobanteResponse enviarCorreoAdmin(Long idComprobante, Long usuarioActor) {
        ComprobanteEntity comprobante = comprobanteRepository.findById(idComprobante)
            .orElseThrow(() -> new NegocioException("comprobante no encontrado"));
        return enviarCorreoSiPendiente(comprobante, usuarioActor);
    }

    @Transactional
    public ComprobanteResponse enviarCorreoPorPedidoSiPendiente(Long idPedido, Long usuarioActor) {
        ComprobanteEntity comprobante = comprobanteRepository.findByIdPedido(idPedido)
            .orElseThrow(() -> new NegocioException("el pedido aun no tiene comprobante emitido"));
        return enviarCorreoSiPendiente(comprobante, usuarioActor);
    }

    private Long obtenerIdUsuario(String emailAutenticado) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));
        return usuario.getIdUsuario();
    }

    private ComprobanteResponse enviarCorreoSiPendiente(ComprobanteEntity comprobante, Long usuarioActor) {
        if (Boolean.TRUE.equals(comprobante.getCorreoEnviado())) {
            return toResponse(comprobante);
        }

        String correoDestino = resolverCorreoDestino(comprobante);
        LocalDateTime ahora = LocalDateTime.now();
        comprobante.setCorreoDestino(correoDestino);
        comprobante.setUsuarioActualizacion(usuarioActor);
        try {
            comprobanteCorreoService.enviar(comprobante, correoDestino);
            comprobante.setCorreoEnviado(true);
            comprobante.setFechaCorreoEnvio(ahora);
            comprobante.setCorreoError(null);
        } catch (RuntimeException e) {
            comprobante.setCorreoEnviado(false);
            comprobante.setFechaCorreoEnvio(null);
            comprobante.setCorreoError(limitarError(e.getMessage()));
        }

        ComprobanteEntity guardado = comprobanteRepository.save(comprobante);
        auditoriaService.registrar(
            "COMPROBANTE",
            String.valueOf(guardado.getIdComprobante()),
            Boolean.TRUE.equals(guardado.getCorreoEnviado()) ? "ENVIAR_COMPROBANTE_CORREO" : "ERROR_COMPROBANTE_CORREO",
            AuditoriaActorTipo.ADMIN,
            usuarioActor,
            AuditoriaCanal.WEB,
            "{\"idPedido\":" + guardado.getIdPedido() + ",\"numero\":\"" + guardado.getNumeroCompleto() + "\",\"correoDestino\":\"" + textoSeguro(correoDestino) + "\"}"
        );
        return toResponse(guardado);
    }

    private String resolverCorreoDestino(ComprobanteEntity comprobante) {
        return pedidoRepository.findById(comprobante.getIdPedido())
            .flatMap(pedido -> usuarioRepository.findById(pedido.getIdCliente()))
            .map(UsuarioEntity::getEmail)
            .map(String::trim)
            .filter(correo -> !correo.isEmpty())
            .orElse(null);
    }

    private String limitarError(String error) {
        String mensaje = error == null || error.isBlank() ? "no se pudo enviar el correo del comprobante" : error.trim();
        return mensaje.length() > 500 ? mensaje.substring(0, 500) : mensaje;
    }

    private String textoSeguro(String valor) {
        return valor == null ? "" : valor.replace("\"", "\\\"");
    }

    private ComprobanteResponse toResponse(ComprobanteEntity c) {
        List<ComprobanteDetalleResponse> detalle = comprobanteDetalleRepository
            .findByIdComprobanteOrderByIdComprobanteDetalleAsc(c.getIdComprobante())
            .stream()
            .map(d -> new ComprobanteDetalleResponse(
                d.getIdComprobanteDetalle(),
                d.getDescripcionItem(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getDescuentoUnitario(),
                d.getSubtotalLinea()
            ))
            .toList();

        return new ComprobanteResponse(
            c.getIdComprobante(),
            c.getIdPedido(),
            c.getTipo().name(),
            c.getSerie(),
            c.getCorrelativo(),
            c.getNumeroCompleto(),
            c.getEstado().name(),
            c.getDocReceptorTipo() == null ? null : c.getDocReceptorTipo().name(),
            c.getDocReceptorNumero(),
            c.getRazonSocialReceptor(),
            c.getDireccionFiscalReceptor(),
            c.getSubtotal(),
            c.getImpuestoTotal(),
            c.getTotal(),
            c.getFechaEmision(),
            Boolean.TRUE.equals(c.getCorreoEnviado()),
            c.getCorreoDestino(),
            c.getFechaCorreoEnvio(),
            c.getCorreoError(),
            c.getPdfPath(),
            c.getPdfToken(),
            c.getFechaPdfGenerado(),
            detalle
        );
    }
}
