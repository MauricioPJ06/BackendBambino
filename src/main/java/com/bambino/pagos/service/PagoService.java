package com.bambino.pagos.service;

import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.carrito.dto.CheckoutValidarRequest;
import com.bambino.carrito.dto.CheckoutValidarResponse;
import com.bambino.carrito.service.CarritoClienteService;
import com.bambino.comprobantes.service.ComprobanteService;
import com.bambino.pagos.dto.PagoCheckoutConfirmarRequest;
import com.bambino.pagos.dto.PagoCheckoutOrdenResponse;
import com.bambino.pagos.dto.PagoCheckoutResponse;
import com.bambino.pagos.dto.PagoConfirmarRequest;
import com.bambino.pagos.dto.PagoIniciarRequest;
import com.bambino.pagos.dto.PagoResponse;
import com.bambino.pagos.dto.PagoWebhookRequest;
import com.bambino.pagos.entity.EstadoPago;
import com.bambino.pagos.entity.MetodoPago;
import com.bambino.pagos.entity.PagoEntity;
import com.bambino.pagos.repository.PagoRepository;
import com.bambino.pagos.service.culqi.CulqiChargeClient;
import com.bambino.pagos.service.culqi.CulqiChargeRequest;
import com.bambino.pagos.service.culqi.CulqiChargeResponse;
import com.bambino.pagos.service.culqi.CulqiOrderClient;
import com.bambino.pagos.service.culqi.CulqiOrderRequest;
import com.bambino.pagos.service.culqi.CulqiOrderResponse;
import com.bambino.pagos.service.culqi.CulqiProperties;
import com.bambino.pedidos.entity.ActorTipoPedido;
import com.bambino.pedidos.entity.EstadoPedido;
import com.bambino.pedidos.entity.PedidoEntity;
import com.bambino.pedidos.entity.PedidoEstadoHistorialEntity;
import com.bambino.pedidos.dto.PedidoCrearDesdeCheckoutRequest;
import com.bambino.pedidos.dto.PedidoResponse;
import com.bambino.pedidos.repository.PedidoEstadoHistorialRepository;
import com.bambino.pedidos.repository.PedidoEstadoTransicionPermitidaRepository;
import com.bambino.pedidos.repository.PedidoRepository;
import com.bambino.pedidos.service.PedidoEstadoService;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
/**
 * Clase que maneja la funcionalidad de PagoService.
 */
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoEstadoTransicionPermitidaRepository transicionRepository;
    private final PedidoEstadoHistorialRepository historialRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComprobanteService comprobanteService;
    private final AuditoriaService auditoriaService;
    private final CarritoClienteService carritoClienteService;
    private final PedidoEstadoService pedidoEstadoService;
    private final CulqiChargeClient culqiChargeClient;
    private final CulqiOrderClient culqiOrderClient;
    private final CulqiProperties culqiProperties;

    public PagoService(PagoRepository pagoRepository,
                       PedidoRepository pedidoRepository,
                       PedidoEstadoTransicionPermitidaRepository transicionRepository,
                       PedidoEstadoHistorialRepository historialRepository,
                       UsuarioRepository usuarioRepository,
                       ComprobanteService comprobanteService,
                       AuditoriaService auditoriaService,
                       CarritoClienteService carritoClienteService,
                       PedidoEstadoService pedidoEstadoService,
                       CulqiChargeClient culqiChargeClient,
                       CulqiOrderClient culqiOrderClient,
                       CulqiProperties culqiProperties) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
        this.transicionRepository = transicionRepository;
        this.historialRepository = historialRepository;
        this.usuarioRepository = usuarioRepository;
        this.comprobanteService = comprobanteService;
        this.auditoriaService = auditoriaService;
        this.carritoClienteService = carritoClienteService;
        this.pedidoEstadoService = pedidoEstadoService;
        this.culqiChargeClient = culqiChargeClient;
        this.culqiOrderClient = culqiOrderClient;
        this.culqiProperties = culqiProperties;
    }

    public PagoCheckoutOrdenResponse crearOrdenCulqiCheckoutCliente(String emailAutenticado, CheckoutValidarRequest request) {
        UsuarioEntity usuario = obtenerUsuario(emailAutenticado);
        Long idCliente = usuario.getIdUsuario();
        CheckoutValidarResponse validacion = carritoClienteService.confirmarCheckout(emailAutenticado, request);
        validarMontoMinimoCulqi(validacion.total());
        String orderNumber = crearNumeroOrdenCheckout(idCliente);
        CulqiOrderResponse orden = culqiOrderClient.crearOrden(new CulqiOrderRequest(
            validacion.total(),
            culqiProperties.currency(),
            emailAutenticado,
            valorCulqiCliente(usuario.getNombres(), "Cliente"),
            valorCulqiCliente(usuario.getApellidos(), "Bambino"),
            telefonoCulqiCliente(usuario),
            orderNumber,
            "Checkout " + orderNumber + " - Bambino Chicken",
            request.docNumero()
        ));
        return new PagoCheckoutOrdenResponse(orden.id(), validacion.total(), culqiProperties.currency());
    }

    @Transactional
    public PagoCheckoutResponse confirmarPagoCheckoutCliente(String emailAutenticado, PagoCheckoutConfirmarRequest request) {
        String proveedor = limpiar(request.proveedor()) == null ? "CULQI" : limpiar(request.proveedor()).toUpperCase(Locale.ROOT);
        String culqiToken = limpiar(request.culqiToken());
        if ("CULQI".equals(proveedor) && culqiToken == null) {
            throw new NegocioException("token Culqi es obligatorio");
        }

        Long idCliente = obtenerIdUsuario(emailAutenticado);
        String idempotencyKey = request.idempotencyKey().trim();

        PagoEntity existente = pagoRepository.findByIdempotencyKey(idempotencyKey).orElse(null);
        if (existente != null) {
            PedidoResponse pedidoExistente = pedidoEstadoService.obtenerPedidoCliente(emailAutenticado, existente.getIdPedido());
            return new PagoCheckoutResponse(pedidoExistente, toResponse(existente));
        }

        CheckoutValidarRequest checkoutRequest = new CheckoutValidarRequest(
            request.modalidad(),
            request.tipoComprobante(),
            request.idDireccion(),
            request.docNumero(),
            request.razonSocial(),
            request.direccionFiscal()
        );
        CheckoutValidarResponse validacion = carritoClienteService.confirmarCheckout(emailAutenticado, checkoutRequest);

        PedidoResponse pedido = pedidoEstadoService.crearPedidoDesdeCheckout(emailAutenticado, new PedidoCrearDesdeCheckoutRequest(
            validacion.modalidad(),
            validacion.tipoComprobante(),
            "DELIVERY".equals(validacion.modalidad()) ? request.idDireccion() : null,
            request.docNumero(),
            request.razonSocial(),
            request.direccionFiscal(),
            validacion.subtotal(),
            validacion.descuentoTotal(),
            validacion.impuestoTotal(),
            validacion.total()
        ));

        PedidoEntity pedidoEntity = pedidoRepository.findById(pedido.idPedido())
            .orElseThrow(() -> new NegocioException("pedido asociado al pago no encontrado"));
        transicionarPedidoSistema(pedidoEntity, EstadoPedido.PAGO_PENDIENTE, "inicio de pago Culqi");

        CulqiChargeResponse cargo = crearCargoCulqi(emailAutenticado, request, pedido, culqiToken);

        PagoEntity pago = new PagoEntity();
        pago.setIdPedido(pedido.idPedido());
        pago.setMetodo(parseMetodo(request.metodo()));
        pago.setEstado(EstadoPago.APROBADO);
        pago.setMonto(pedido.total());
        pago.setProveedor(proveedor);
        pago.setProveedorTxnId(cargo.id());
        pago.setIdempotencyKey(idempotencyKey);
        pago.setUrlPago(null);
        pago.setUsuarioCreacion(idCliente);
        pago.setUsuarioActualizacion(idCliente);
        pago.setFechaCreacion(LocalDateTime.now());
        pago.setFechaActualizacion(LocalDateTime.now());
        pago = pagoRepository.save(pago);

        transicionarPedidoSistema(pedidoEntity, EstadoPedido.PAGO_APROBADO, "pago Culqi aprobado");
        comprobanteService.emitirSiNoExiste(pedido.idPedido(), idCliente);
        comprobanteService.enviarCorreoPorPedidoSiPendiente(pedido.idPedido(), idCliente);

        auditoriaService.registrar(
            "PAGO",
            String.valueOf(pago.getIdPago()),
            "CONFIRMAR_PAGO_CHECKOUT",
            AuditoriaActorTipo.CLIENTE,
            idCliente,
            AuditoriaCanal.WEB,
            "{\"idPedido\":" + pago.getIdPedido() + ",\"estado\":\"" + pago.getEstado().name() + "\"}"
        );

        PedidoResponse pedidoActualizado = pedidoEstadoService.obtenerPedidoCliente(emailAutenticado, pedido.idPedido());
        return new PagoCheckoutResponse(pedidoActualizado, toResponse(pago));
    }

    private CulqiChargeResponse crearCargoCulqi(String emailAutenticado,
                                                PagoCheckoutConfirmarRequest request,
                                                PedidoResponse pedido,
                                                String culqiToken) {
        return culqiChargeClient.crearCargo(new CulqiChargeRequest(
            pedido.total(),
            culqiProperties.currency(),
            emailAutenticado,
            culqiToken,
            "Pedido " + pedido.codigoPedido() + " - Bambino Chicken",
            pedido.codigoPedido(),
            request.docNumero()
        ));
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de iniciarPagoCliente.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PagoResponse iniciarPagoCliente(String emailAutenticado, PagoIniciarRequest request) {
        Long idCliente = obtenerIdUsuario(emailAutenticado);
        PedidoEntity pedido = obtenerPedidoCliente(request.idPedido(), idCliente);

        PagoEntity existente = pagoRepository.findByIdempotencyKey(request.idempotencyKey().trim())
            .orElse(null);
        if (existente != null) {
            if (!existente.getIdPedido().equals(pedido.getIdPedido())) {
                throw new NegocioException("idempotencyKey ya existe para otro pedido");
            }
            return toResponse(existente);
        }

        if (request.monto().compareTo(pedido.getTotal()) != 0) {
            throw new NegocioException("monto de pago no coincide con total del pedido");
        }

        transicionarPedidoSistema(pedido, EstadoPedido.PAGO_PENDIENTE, "inicio de pago");

        LocalDateTime ahora = LocalDateTime.now();
        PagoEntity pago = new PagoEntity();
        pago.setIdPedido(pedido.getIdPedido());
        pago.setMetodo(parseMetodo(request.metodo()));
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMonto(request.monto());
        pago.setProveedor(limpiar(request.proveedor()));
        pago.setProveedorTxnId(null);
        pago.setIdempotencyKey(request.idempotencyKey().trim());
        pago.setUrlPago(null);
        pago.setUsuarioCreacion(idCliente);
        pago.setUsuarioActualizacion(idCliente);
        pago.setFechaCreacion(ahora);
        pago.setFechaActualizacion(ahora);

        PagoEntity guardado = pagoRepository.save(pago);
        auditoriaService.registrar(
            "PAGO",
            String.valueOf(guardado.getIdPago()),
            "INICIAR_PAGO",
            AuditoriaActorTipo.CLIENTE,
            idCliente,
            AuditoriaCanal.WEB,
            "{\"idPedido\":" + guardado.getIdPedido() + ",\"estado\":\"" + guardado.getEstado().name() + "\"}"
        );
        return toResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de confirmarPagoAdmin.
     * @param idPago parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PagoResponse confirmarPagoAdmin(Long idPago, PagoConfirmarRequest request, String emailAutenticado) {
        Long actorId = obtenerIdUsuario(emailAutenticado);
        PagoEntity pago = pagoRepository.findById(idPago)
            .orElseThrow(() -> new NegocioException("pago no encontrado"));

        EstadoPago nuevoEstado = parseEstadoPago(request.estado());
        if (nuevoEstado != EstadoPago.APROBADO && nuevoEstado != EstadoPago.RECHAZADO) {
            throw new NegocioException("solo se permite confirmar pago APROBADO o RECHAZADO");
        }

        pago.setEstado(nuevoEstado);
        pago.setProveedorTxnId(limpiar(request.proveedorTxnId()));
        pago.setUsuarioActualizacion(actorId);
        pago.setFechaActualizacion(LocalDateTime.now());
        pago = pagoRepository.save(pago);

        PedidoEntity pedido = pedidoRepository.findById(pago.getIdPedido())
            .orElseThrow(() -> new NegocioException("pedido asociado al pago no encontrado"));

        if (nuevoEstado == EstadoPago.APROBADO) {
            transicionarPedidoSistema(pedido, EstadoPedido.PAGO_APROBADO, request.motivo());
            comprobanteService.emitirSiNoExiste(pedido.getIdPedido(), actorId);
            comprobanteService.enviarCorreoPorPedidoSiPendiente(pedido.getIdPedido(), actorId);
        } else {
            transicionarPedidoSistema(pedido, EstadoPedido.PAGO_RECHAZADO, request.motivo());
        }
        auditoriaService.registrar(
            "PAGO",
            String.valueOf(pago.getIdPago()),
            "CONFIRMAR_PAGO",
            AuditoriaActorTipo.ADMIN,
            actorId,
            AuditoriaCanal.WEB,
            "{\"idPedido\":" + pago.getIdPedido() + ",\"estado\":\"" + pago.getEstado().name() + "\"}"
        );

        return toResponse(pago);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de confirmarPagoWebhookPublico.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PagoResponse confirmarPagoWebhookPublico(PagoWebhookRequest request) {
        String idempotencyKey = request.idempotencyKey().trim();
        PagoEntity pago = pagoRepository.findByIdempotencyKey(idempotencyKey)
            .orElseThrow(() -> new NegocioException("pago no encontrado para idempotencyKey"));

        EstadoPago nuevoEstado = parseEstadoPago(request.estado());
        if (nuevoEstado != EstadoPago.APROBADO && nuevoEstado != EstadoPago.RECHAZADO) {
            throw new NegocioException("webhook solo permite estado APROBADO o RECHAZADO");
        }
        if (pago.getEstado() == nuevoEstado) {
            return toResponse(pago);
        }

        pago.setEstado(nuevoEstado);
        pago.setProveedorTxnId(limpiar(request.proveedorTxnId()));
        pago.setFechaActualizacion(LocalDateTime.now());
        pago = pagoRepository.save(pago);

        PedidoEntity pedido = pedidoRepository.findById(pago.getIdPedido())
            .orElseThrow(() -> new NegocioException("pedido asociado al pago no encontrado"));

        if (nuevoEstado == EstadoPago.APROBADO) {
            transicionarPedidoSistema(pedido, EstadoPedido.PAGO_APROBADO, request.motivo());
            comprobanteService.emitirSiNoExiste(pedido.getIdPedido(), null);
            comprobanteService.enviarCorreoPorPedidoSiPendiente(pedido.getIdPedido(), null);
        } else {
            transicionarPedidoSistema(pedido, EstadoPedido.PAGO_RECHAZADO, request.motivo());
        }
        auditoriaService.registrar(
            "PAGO",
            String.valueOf(pago.getIdPago()),
            "CONFIRMAR_PAGO_WEBHOOK",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"idPedido\":" + pago.getIdPedido() + ",\"estado\":\"" + pago.getEstado().name() + "\",\"idempotencyKey\":\"" + idempotencyKey + "\"}"
        );

        return toResponse(pago);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarPagosClientePorPedido.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<PagoResponse> listarPagosClientePorPedido(String emailAutenticado, Long idPedido) {
        Long idCliente = obtenerIdUsuario(emailAutenticado);
        obtenerPedidoCliente(idPedido, idCliente);
        return pagoRepository.findByIdPedidoOrderByFechaCreacionDesc(idPedido).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarPagosAdmin.
     * @return resultado de la operacion
     */
    public List<PagoResponse> listarPagosAdmin() {
        return pagoRepository.findAllByOrderByFechaCreacionDesc().stream().map(this::toResponse).toList();
    }

    private void transicionarPedidoSistema(PedidoEntity pedido, EstadoPedido destino, String motivo) {
        EstadoPedido origen = pedido.getEstadoActual();
        if (origen == destino) {
            return;
        }

        transicionRepository.findByEstadoOrigenAndEstadoDestinoAndActorTipoAndActivoTrue(origen, destino, ActorTipoPedido.SISTEMA)
            .orElseThrow(() -> new NegocioException("transicion de pedido no permitida para sistema"));

        pedido.setEstadoActual(destino);
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedidoRepository.save(pedido);
        pedidoRepository.flush();

        PedidoEstadoHistorialEntity historial = new PedidoEstadoHistorialEntity();
        historial.setIdPedido(pedido.getIdPedido());
        historial.setEstadoOrigen(origen);
        historial.setEstadoDestino(destino);
        historial.setActorId(null);
        historial.setActorTipo(ActorTipoPedido.SISTEMA);
        historial.setMotivo(limpiar(motivo));
        historial.setFechaCreacion(LocalDateTime.now());
        historialRepository.save(historial);
    }

    private PedidoEntity obtenerPedidoCliente(Long idPedido, Long idCliente) {
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new NegocioException("el pedido no pertenece al cliente autenticado");
        }

        return pedido;
    }

    private Long obtenerIdUsuario(String emailAutenticado) {
        return obtenerUsuario(emailAutenticado).getIdUsuario();
    }

    private UsuarioEntity obtenerUsuario(String emailAutenticado) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));
        return usuario;
    }

    private MetodoPago parseMetodo(String valor) {
        try {
            return MetodoPago.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new NegocioException("metodo de pago invalido");
        }
    }

    private EstadoPago parseEstadoPago(String valor) {
        try {
            return EstadoPago.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new NegocioException("estado de pago invalido");
        }
    }

    private String limpiar(String valor) {
        if (valor == null) {
            return null;
        }
        String v = valor.trim();
        return v.isEmpty() ? null : v;
    }

    private String crearNumeroOrdenCheckout(Long idCliente) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        return "CHK-" + idCliente + "-" + timestamp;
    }

    private void validarMontoMinimoCulqi(java.math.BigDecimal total) {
        java.math.BigDecimal minimo = culqiProperties.minimumOrderAmount();
        if (total.compareTo(minimo) < 0) {
            throw new NegocioException("el monto minimo para pagar con Culqi es S/ " + minimo.setScale(2));
        }
    }

    private String valorCulqiCliente(String valor, String fallback) {
        String limpio = limpiar(valor);
        String base = limpio == null ? fallback : limpio;
        return base.length() > 49 ? base.substring(0, 49) : base;
    }

    private String telefonoCulqiCliente(UsuarioEntity usuario) {
        if (culqiProperties.testMode()) {
            return culqiProperties.sandboxPhoneNumber();
        }
        String telefono = normalizarTelefonoCulqi(usuario.getTelefono());
        if (telefono == null) {
            throw new NegocioException("telefono del cliente es obligatorio para pagar con Culqi");
        }
        return telefono;
    }

    private String normalizarTelefonoCulqi(String telefono) {
        String limpio = limpiar(telefono);
        if (limpio == null) {
            return null;
        }
        String normalizado = limpio.replaceAll("[^0-9+]", "");
        if (normalizado.length() <= 5 || normalizado.length() >= 15) {
            return null;
        }
        return normalizado;
    }

    private PagoResponse toResponse(PagoEntity p) {
        return new PagoResponse(
            p.getIdPago(),
            p.getIdPedido(),
            p.getMetodo().name(),
            p.getEstado().name(),
            p.getMonto(),
            p.getProveedor(),
            p.getProveedorTxnId(),
            p.getIdempotencyKey(),
            p.getUrlPago(),
            p.getFechaCreacion(),
            p.getFechaActualizacion()
        );
    }
}
