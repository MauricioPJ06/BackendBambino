package com.bambino.pedidos.service;

import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.carrito.entity.CarritoEntity;
import com.bambino.carrito.entity.CarritoItemEntity;
import com.bambino.carrito.entity.EstadoCarrito;
import com.bambino.carrito.repository.CarritoItemRepository;
import com.bambino.carrito.repository.CarritoRepository;
import com.bambino.catalogo.entity.ProductoEntity;
import com.bambino.catalogo.repository.ProductoRepository;
import com.bambino.clientes.entity.ClienteDireccionEntity;
import com.bambino.clientes.repository.ClienteDireccionRepository;
import com.bambino.pedidos.dto.PedidoCambiarEstadoRequest;
import com.bambino.pedidos.dto.PedidoAsignarRequest;
import com.bambino.pedidos.dto.PedidoCrearDesdeCheckoutRequest;
import com.bambino.pedidos.dto.PedidoEstadoHistorialResponse;
import com.bambino.pedidos.dto.PedidoResponse;
import com.bambino.pedidos.dto.PedidoAsignacionCocinaResponse;
import com.bambino.pedidos.dto.PedidoCocinaIncidenciaRequest;
import com.bambino.pedidos.dto.PedidoCocinaIncidenciaResponse;
import com.bambino.pedidos.entity.*;
import com.bambino.pedidos.repository.PedidoEstadoHistorialRepository;
import com.bambino.pedidos.repository.PedidoEstadoTransicionPermitidaRepository;
import com.bambino.pedidos.repository.PedidoAsignacionCocinaRepository;
import com.bambino.pedidos.repository.PedidoItemRepository;
import com.bambino.pedidos.repository.PedidoCocinaIncidenciaRepository;
import com.bambino.pedidos.repository.PedidoRepository;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
/**
 * Clase que maneja la funcionalidad de PedidoEstadoService.
 */
public class PedidoEstadoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoEstadoHistorialRepository historialRepository;
    private final PedidoEstadoTransicionPermitidaRepository transicionRepository;
    private final PedidoAsignacionCocinaRepository asignacionCocinaRepository;
    private final PedidoCocinaIncidenciaRepository incidenciaRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final ClienteDireccionRepository clienteDireccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;

    public PedidoEstadoService(PedidoRepository pedidoRepository,
                               PedidoEstadoHistorialRepository historialRepository,
                               PedidoEstadoTransicionPermitidaRepository transicionRepository,
                               PedidoAsignacionCocinaRepository asignacionCocinaRepository,
                               PedidoCocinaIncidenciaRepository incidenciaRepository,
                               PedidoItemRepository pedidoItemRepository,
                               CarritoRepository carritoRepository,
                               CarritoItemRepository carritoItemRepository,
                               ProductoRepository productoRepository,
                               ClienteDireccionRepository clienteDireccionRepository,
                               UsuarioRepository usuarioRepository,
                               AuditoriaService auditoriaService) {
        this.pedidoRepository = pedidoRepository;
        this.historialRepository = historialRepository;
        this.transicionRepository = transicionRepository;
        this.asignacionCocinaRepository = asignacionCocinaRepository;
        this.incidenciaRepository = incidenciaRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.productoRepository = productoRepository;
        this.clienteDireccionRepository = clienteDireccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearPedidoDesdeCheckout.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PedidoResponse crearPedidoDesdeCheckout(String emailAutenticado, PedidoCrearDesdeCheckoutRequest request) {
        UsuarioEntity cliente = obtenerUsuarioAutenticado(emailAutenticado);
        Long idCliente = cliente.getIdUsuario();
        LocalDateTime ahora = LocalDateTime.now();
        CarritoEntity carrito = obtenerCarritoConvertible(idCliente);

        PedidoEntity pedido = new PedidoEntity();
        TipoComprobantePedido tipoComprobante = parseTipoComprobante(request.tipoComprobante());
        String docNumero = limpiarTexto(request.docNumero());
        String razonSocial = resolverRazonSocial(request.razonSocial(), cliente, tipoComprobante);
        String direccionFiscal = resolverDireccionFiscal(request.direccionFiscal(), idCliente, request.idDireccionEntrega(), tipoComprobante);
        validarDatosFactura(tipoComprobante, docNumero);

        pedido.setCodigoPedido(generarCodigoPedidoTemporal());
        pedido.setIdCliente(idCliente);
        pedido.setCanalOrigen(CanalPedido.WEB);
        pedido.setModalidad(parseModalidad(request.modalidad()));
        pedido.setTipoComprobante(tipoComprobante);
        pedido.setDocReceptorNumero(docNumero);
        pedido.setDocReceptorTipo(determinarDocReceptor(request.tipoComprobante(), docNumero));
        pedido.setRazonSocialSnapshot(razonSocial);
        pedido.setDireccionFiscalSnapshot(direccionFiscal);
        pedido.setEstadoActual(EstadoPedido.CREADO);
        pedido.setIdDireccionEntrega(request.idDireccionEntrega());
        pedido.setSubtotal(request.subtotal());
        pedido.setDescuentoTotal(request.descuentoTotal());
        pedido.setImpuestoTotal(request.impuestoTotal());
        pedido.setTotal(request.total());
        pedido.setUsuarioCreacion(idCliente);
        pedido.setUsuarioActualizacion(idCliente);
        pedido.setFechaCreacion(ahora);
        pedido.setFechaActualizacion(ahora);

        PedidoEntity guardado = pedidoRepository.save(pedido);
        guardado.setCodigoPedido(generarCodigoPedidoSecuencial(guardado.getIdPedido()));
        guardado = pedidoRepository.save(guardado);
        crearPedidoItemsDesdeCarrito(carrito.getIdCarrito(), guardado.getIdPedido(), ahora);
        carrito.setEstado(EstadoCarrito.CONVERTIDO);
        carrito.setUsuarioActualizacion(idCliente);
        carrito.setFechaActualizacion(ahora);
        carritoRepository.save(carrito);
        registrarHistorial(guardado.getIdPedido(), null, EstadoPedido.CREADO, idCliente, ActorTipoPedido.CLIENTE, "creacion de pedido desde checkout");
        auditoriaService.registrar(
            "PEDIDO",
            String.valueOf(guardado.getIdPedido()),
            "CREAR_PEDIDO",
            AuditoriaActorTipo.CLIENTE,
            idCliente,
            AuditoriaCanal.WEB,
            "{\"codigoPedido\":\"" + guardado.getCodigoPedido() + "\",\"estado\":\"" + guardado.getEstadoActual().name() + "\"}"
        );
        return toResponse(guardado);
    }

    private CarritoEntity obtenerCarritoConvertible(Long idCliente) {
        return carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(idCliente, EstadoCarrito.ABIERTO)
            .or(() -> carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(idCliente, EstadoCarrito.CONFIRMADO))
            .orElseThrow(() -> new NegocioException("no existe carrito abierto o confirmado para crear pedido"));
    }

    private DocReceptorPedido determinarDocReceptor(String tipoComprobante, String docNumero) {
        String tipo = tipoComprobante == null ? "" : tipoComprobante.trim().toUpperCase(Locale.ROOT);
        String doc = limpiarTexto(docNumero);
        if (doc == null) {
            throw new NegocioException("docNumero es obligatorio para crear pedido");
        }
        if ("FACTURA".equals(tipo)) {
            if (doc.length() != 11) {
                throw new NegocioException("factura requiere RUC de 11 digitos");
            }
            return DocReceptorPedido.RUC;
        }
        if (doc.length() == 8) {
            return DocReceptorPedido.DNI;
        }
        if (doc.length() == 11) {
            return DocReceptorPedido.RUC;
        }
        return DocReceptorPedido.OTRO;
    }

    private void validarDatosFactura(TipoComprobantePedido tipoComprobante, String docNumero) {
        if (tipoComprobante != TipoComprobantePedido.FACTURA) {
            return;
        }
        if (docNumero == null || docNumero.length() != 11) {
            throw new NegocioException("factura requiere RUC valido de 11 digitos");
        }
    }

    private String resolverRazonSocial(String razonSocialRequest,
                                       UsuarioEntity cliente,
                                       TipoComprobantePedido tipoComprobante) {
        String razonSocial = limpiarTexto(razonSocialRequest);
        if (razonSocial != null || tipoComprobante != TipoComprobantePedido.FACTURA) {
            return razonSocial;
        }

        String nombreCompleto = (nvl(cliente.getNombres()) + " " + nvl(cliente.getApellidos())).trim();
        return nombreCompleto.isEmpty() ? null : nombreCompleto;
    }

    private String resolverDireccionFiscal(String direccionFiscalRequest,
                                           Long idCliente,
                                           Long idDireccionEntrega,
                                           TipoComprobantePedido tipoComprobante) {
        String direccionFiscal = limpiarTexto(direccionFiscalRequest);
        if (direccionFiscal != null || tipoComprobante != TipoComprobantePedido.FACTURA) {
            return limitarTexto(direccionFiscal, 300);
        }

        ClienteDireccionEntity direccion = null;
        if (idDireccionEntrega != null) {
            direccion = clienteDireccionRepository.findByIdDireccionAndIdClienteAndActivoTrue(idDireccionEntrega, idCliente).orElse(null);
        }
        if (direccion == null) {
            direccion = clienteDireccionRepository.findFirstByIdClienteAndActivoTrueAndEsPrincipalTrue(idCliente).orElse(null);
        }
        if (direccion == null) {
            return "Direccion fiscal no especificada";
        }

        String texto = String.join(", ",
            List.of(
                nvl(direccion.getDireccionLinea1()).trim(),
                nvl(direccion.getDistrito()).trim(),
                nvl(direccion.getCiudad()).trim()
            ).stream().filter(valor -> !valor.isBlank()).toList()
        );
        if (texto.isBlank()) {
            texto = "Direccion fiscal no especificada";
        }
        return limitarTexto(texto, 300);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de cancelarPedidoCliente.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @param motivo parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PedidoResponse cancelarPedidoCliente(String emailAutenticado, Long idPedido, String motivo) {
        Long actorId = obtenerIdUsuario(emailAutenticado);
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));
        if (!pedido.getIdCliente().equals(actorId)) {
            throw new NegocioException("no tiene acceso a este pedido");
        }
        PedidoCambiarEstadoRequest request = new PedidoCambiarEstadoRequest("CANCELADO", motivo == null ? "cancelacion de cliente" : motivo);
        return cambiarEstado(idPedido, request, actorId, ActorTipoPedido.CLIENTE);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarPedidosCliente.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<PedidoResponse> listarPedidosCliente(String emailAutenticado) {
        Long idCliente = obtenerIdUsuario(emailAutenticado);
        return pedidoRepository.findByIdClienteOrderByFechaCreacionDesc(idCliente).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerPedidoCliente.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PedidoResponse obtenerPedidoCliente(String emailAutenticado, Long idPedido) {
        Long idCliente = obtenerIdUsuario(emailAutenticado);
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new NegocioException("no tiene acceso a este pedido");
        }

        return toResponse(pedido);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerPedidoClientePorCodigo.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param codigoPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PedidoResponse obtenerPedidoClientePorCodigo(String emailAutenticado, String codigoPedido) {
        Long idCliente = obtenerIdUsuario(emailAutenticado);
        PedidoEntity pedido = pedidoRepository.findByCodigoPedido(codigoPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new NegocioException("no tiene acceso a este pedido");
        }

        return toResponse(pedido);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de historialPedidoCliente.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<PedidoEstadoHistorialResponse> historialPedidoCliente(String emailAutenticado, Long idPedido) {
        Long idCliente = obtenerIdUsuario(emailAutenticado);
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new NegocioException("no tiene acceso a este pedido");
        }

        return historialRepository.findByIdPedidoOrderByFechaCreacionAsc(idPedido).stream().map(this::toHistorialResponse).toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarPedidosAdmin.
     * @return resultado de la operacion
     */
    public List<PedidoResponse> listarPedidosAdmin() {
        return pedidoRepository.findAllByOrderByFechaCreacionDesc().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarPedidosCocinaOperativos.
     * @return resultado de la operacion
     */
    public List<PedidoResponse> listarPedidosCocinaOperativos() {
        return pedidoRepository.findByEstadoActualInOrderByFechaCreacionAsc(
            List.of(EstadoPedido.CONFIRMADO, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO_RECOJO, EstadoPedido.LISTO_DESPACHO)
        ).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarAsignacionesCocina.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<PedidoAsignacionCocinaResponse> listarAsignacionesCocina(String emailAutenticado, Long idPedido) {
        obtenerIdUsuario(emailAutenticado);
        pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));
        return asignacionCocinaRepository.findByIdPedidoOrderByFechaAsignacionDesc(idPedido).stream()
            .map(a -> new PedidoAsignacionCocinaResponse(
                a.getIdAsignacion(),
                a.getIdPedido(),
                a.getIdUsuarioCocina(),
                a.getMotivo(),
                a.getFechaAsignacion()
            ))
            .toList();
    }

    @Transactional
    public PedidoCocinaIncidenciaResponse registrarIncidenciaCocina(String emailAutenticado,
                                                                     Long idPedido,
                                                                     PedidoCocinaIncidenciaRequest request) {
        Long actorId = obtenerIdUsuario(emailAutenticado);
        pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));

        PedidoCocinaIncidenciaEntity incidencia = new PedidoCocinaIncidenciaEntity();
        incidencia.setIdPedido(idPedido);
        incidencia.setIdUsuarioCocina(actorId);
        incidencia.setTipoIncidencia(request.tipoIncidencia().trim().toUpperCase(Locale.ROOT));
        incidencia.setDetalle(request.detalle().trim());
        incidencia.setFechaCreacion(LocalDateTime.now());
        PedidoCocinaIncidenciaEntity guardada = incidenciaRepository.save(incidencia);

        auditoriaService.registrar(
            "PEDIDO_COCINA_INCIDENCIA",
            String.valueOf(guardada.getIdIncidencia()),
            "REGISTRAR_INCIDENCIA_COCINA",
            AuditoriaActorTipo.COCINA,
            actorId,
            AuditoriaCanal.WEB,
            "{\"idPedido\":" + idPedido + ",\"tipo\":\"" + guardada.getTipoIncidencia() + "\"}"
        );

        return new PedidoCocinaIncidenciaResponse(
            guardada.getIdIncidencia(),
            guardada.getIdPedido(),
            guardada.getIdUsuarioCocina(),
            guardada.getTipoIncidencia(),
            guardada.getDetalle(),
            guardada.getFechaCreacion()
        );
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarIncidenciasCocina.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<PedidoCocinaIncidenciaResponse> listarIncidenciasCocina(String emailAutenticado, Long idPedido) {
        obtenerIdUsuario(emailAutenticado);
        pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));
        return incidenciaRepository.findByIdPedidoOrderByFechaCreacionDesc(idPedido).stream()
            .map(i -> new PedidoCocinaIncidenciaResponse(
                i.getIdIncidencia(),
                i.getIdPedido(),
                i.getIdUsuarioCocina(),
                i.getTipoIncidencia(),
                i.getDetalle(),
                i.getFechaCreacion()
            ))
            .toList();
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de cambiarEstadoAdmin.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PedidoResponse cambiarEstadoAdmin(String emailAutenticado, Long idPedido, PedidoCambiarEstadoRequest request) {
        Long actorId = obtenerIdUsuario(emailAutenticado);
        return cambiarEstado(idPedido, request, actorId, ActorTipoPedido.ADMIN);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de cambiarEstadoCocina.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PedidoResponse cambiarEstadoCocina(String emailAutenticado, Long idPedido, PedidoCambiarEstadoRequest request) {
        Long actorId = obtenerIdUsuario(emailAutenticado);
        return cambiarEstado(idPedido, request, actorId, ActorTipoPedido.COCINA);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de tomarPedidoCocina.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PedidoResponse tomarPedidoCocina(String emailAutenticado, Long idPedido, PedidoAsignarRequest request) {
        Long actorId = obtenerIdUsuario(emailAutenticado);
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));

        PedidoAsignacionCocinaEntity asignacion = new PedidoAsignacionCocinaEntity();
        asignacion.setIdPedido(idPedido);
        asignacion.setIdUsuarioCocina(actorId);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        asignacion.setMotivo(request.motivo() == null ? "toma de pedido cocina" : request.motivo().trim());
        asignacionCocinaRepository.save(asignacion);

        if (pedido.getUsuarioCocinaPreparacion() == null) {
            pedido.setUsuarioCocinaPreparacion(actorId);
            pedido.setUsuarioActualizacion(actorId);
            pedido.setFechaActualizacion(LocalDateTime.now());
            pedido = pedidoRepository.save(pedido);
        }
        auditoriaService.registrar(
            "PEDIDO",
            String.valueOf(pedido.getIdPedido()),
            "TOMAR_PEDIDO_COCINA",
            AuditoriaActorTipo.COCINA,
            actorId,
            AuditoriaCanal.WEB,
            "{\"motivo\":\"" + (asignacion.getMotivo() == null ? "" : asignacion.getMotivo()) + "\"}"
        );

        return toResponse(pedido);
    }

    private PedidoResponse cambiarEstado(Long idPedido, PedidoCambiarEstadoRequest request, Long actorId, ActorTipoPedido actorTipo) {
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));

        EstadoPedido origen = pedido.getEstadoActual();
        EstadoPedido destino = parseEstado(request.estadoDestino());
        validarMotivoObligatorio(destino, request.motivo());

        transicionRepository.findByEstadoOrigenAndEstadoDestinoAndActorTipoAndActivoTrue(origen, destino, actorTipo)
            .orElseThrow(() -> new NegocioException("transicion de estado no permitida para el rol"));

        if (actorTipo == ActorTipoPedido.COCINA && destino == EstadoPedido.EN_PREPARACION) {
            if (pedido.getUsuarioCocinaPreparacion() == null) {
                pedido.setUsuarioCocinaPreparacion(actorId);
            }
            if (pedido.getFechaInicioPreparacion() == null) {
                pedido.setFechaInicioPreparacion(LocalDateTime.now());
            }
        }

        if (actorTipo == ActorTipoPedido.COCINA
            && (destino == EstadoPedido.LISTO_RECOJO || destino == EstadoPedido.LISTO_DESPACHO)
            && pedido.getFechaFinPreparacion() == null) {
            pedido.setFechaFinPreparacion(LocalDateTime.now());
        }

        pedido.setEstadoActual(destino);
        pedido.setUsuarioActualizacion(actorId);
        pedido.setFechaActualizacion(LocalDateTime.now());
        PedidoEntity guardado = pedidoRepository.save(pedido);

        registrarHistorial(idPedido, origen, destino, actorId, actorTipo, request.motivo());
        auditoriaService.registrar(
            "PEDIDO",
            String.valueOf(idPedido),
            "CAMBIAR_ESTADO",
            actorTipo == ActorTipoPedido.COCINA ? AuditoriaActorTipo.COCINA : actorTipo == ActorTipoPedido.CLIENTE ? AuditoriaActorTipo.CLIENTE : AuditoriaActorTipo.ADMIN,
            actorId,
            AuditoriaCanal.WEB,
            "{\"origen\":\"" + origen.name() + "\",\"destino\":\"" + destino.name() + "\"}"
        );
        return toResponse(guardado);
    }

    private void validarMotivoObligatorio(EstadoPedido destino, String motivo) {
        if ((destino == EstadoPedido.CANCELADO || destino == EstadoPedido.ANULADO || destino == EstadoPedido.PAGO_RECHAZADO)
            && (motivo == null || motivo.isBlank())) {
            throw new NegocioException("motivo es obligatorio para el estado de destino");
        }
    }

    private void registrarHistorial(Long idPedido,
                                    EstadoPedido origen,
                                    EstadoPedido destino,
                                    Long actorId,
                                    ActorTipoPedido actorTipo,
                                    String motivo) {
        PedidoEstadoHistorialEntity historial = new PedidoEstadoHistorialEntity();
        historial.setIdPedido(idPedido);
        historial.setEstadoOrigen(origen);
        historial.setEstadoDestino(destino);
        historial.setActorId(actorId);
        historial.setActorTipo(actorTipo);
        historial.setMotivo(motivo == null ? null : motivo.trim());
        historial.setFechaCreacion(LocalDateTime.now());
        historialRepository.save(historial);
    }

    private Long obtenerIdUsuario(String emailAutenticado) {
        return obtenerUsuarioAutenticado(emailAutenticado).getIdUsuario();
    }

    private UsuarioEntity obtenerUsuarioAutenticado(String emailAutenticado) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));
        return usuario;
    }

    private String generarCodigoPedidoTemporal() {
        return "TMP-" + UUID.randomUUID().toString().replace("-", "").substring(0, 24).toUpperCase(Locale.ROOT);
    }

    private String generarCodigoPedidoSecuencial(Long idPedido) {
        return "PED-" + String.format(Locale.ROOT, "%09d", idPedido);
    }

    private void crearPedidoItemsDesdeCarrito(Long idCarrito, Long idPedido, LocalDateTime fecha) {
        List<CarritoItemEntity> items = carritoItemRepository.findByIdCarritoOrderByIdCarritoItemAsc(idCarrito);
        if (items.isEmpty()) {
            throw new NegocioException("carrito sin items para crear pedido");
        }

        for (CarritoItemEntity item : items) {
            ProductoEntity producto = productoRepository.findById(item.getIdProducto())
                .orElseThrow(() -> new NegocioException("producto no encontrado para pedido_item"));

            PedidoItemEntity pItem = new PedidoItemEntity();
            pItem.setIdPedido(idPedido);
            pItem.setIdProducto(item.getIdProducto());
            pItem.setNombreProductoSnapshot(producto.getNombre());
            pItem.setCantidad(item.getCantidad());
            pItem.setPrecioUnitario(item.getPrecioUnitarioSnapshot());
            pItem.setDescuentoUnitario(item.getDescuentoUnitarioSnapshot());
            pItem.setSubtotalLinea(item.getPrecioUnitarioSnapshot().subtract(item.getDescuentoUnitarioSnapshot()).multiply(item.getCantidad()));
            pItem.setFechaCreacion(fecha);
            pedidoItemRepository.save(pItem);
        }
    }

    private ModalidadPedido parseModalidad(String valor) {
        try {
            return ModalidadPedido.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new NegocioException("modalidad de pedido invalida");
        }
    }

    private TipoComprobantePedido parseTipoComprobante(String valor) {
        try {
            return TipoComprobantePedido.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new NegocioException("tipo de comprobante invalido");
        }
    }

    private EstadoPedido parseEstado(String valor) {
        try {
            return EstadoPedido.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new NegocioException("estado de pedido invalido");
        }
    }

    private String limpiarTexto(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private String nvl(String valor) {
        return valor == null ? "" : valor;
    }

    private String limitarTexto(String valor, int maxLength) {
        if (valor == null || valor.length() <= maxLength) {
            return valor;
        }
        return valor.substring(0, maxLength);
    }

    private PedidoResponse toResponse(PedidoEntity p) {
        return new PedidoResponse(
            p.getIdPedido(),
            p.getCodigoPedido(),
            enumNameOrDefault(p.getEstadoActual(), "NO_DEFINIDO"),
            enumNameOrDefault(p.getModalidad(), "NO_DEFINIDO"),
            enumNameOrDefault(p.getTipoComprobante(), "NO_DEFINIDO"),
            p.getSubtotal(),
            p.getDescuentoTotal(),
            p.getImpuestoTotal(),
            p.getTotal(),
            p.getFechaCreacion()
        );
    }

    private String enumNameOrDefault(Enum<?> valor, String porDefecto) {
        return valor == null ? porDefecto : valor.name();
    }

    private PedidoEstadoHistorialResponse toHistorialResponse(PedidoEstadoHistorialEntity h) {
        return new PedidoEstadoHistorialResponse(
            h.getEstadoOrigen() == null ? null : h.getEstadoOrigen().name(),
            h.getEstadoDestino().name(),
            h.getActorTipo().name(),
            h.getActorId(),
            h.getMotivo(),
            h.getFechaCreacion()
        );
    }
}
