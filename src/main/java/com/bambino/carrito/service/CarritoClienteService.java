package com.bambino.carrito.service;

import com.bambino.carrito.dto.CarritoItemActualizarRequest;
import com.bambino.carrito.dto.CarritoItemAgregarRequest;
import com.bambino.carrito.dto.CarritoItemsAgregarRequest;
import com.bambino.carrito.dto.CarritoItemResponse;
import com.bambino.carrito.dto.CarritoResumenResponse;
import com.bambino.carrito.dto.CheckoutValidarRequest;
import com.bambino.carrito.dto.CheckoutValidarResponse;
import com.bambino.carrito.entity.CanalCarrito;
import com.bambino.carrito.entity.CarritoEntity;
import com.bambino.carrito.entity.CarritoItemEntity;
import com.bambino.carrito.entity.ConfiguracionGlobalEntity;
import com.bambino.carrito.entity.EstadoCarrito;
import com.bambino.carrito.entity.ZonaDeliveryEntity;
import com.bambino.carrito.mapper.CarritoMapper;
import com.bambino.carrito.repository.CarritoItemRepository;
import com.bambino.carrito.repository.CarritoRepository;
import com.bambino.carrito.repository.ConfiguracionGlobalRepository;
import com.bambino.carrito.repository.ZonaDeliveryRepository;
import com.bambino.catalogo.entity.EstadoProducto;
import com.bambino.catalogo.entity.OfertaEntity;
import com.bambino.catalogo.entity.OfertaProductoEntity;
import com.bambino.catalogo.entity.TipoOferta;
import com.bambino.catalogo.entity.ProductoEntity;
import com.bambino.catalogo.repository.OfertaProductoRepository;
import com.bambino.catalogo.repository.ProductoRepository;
import com.bambino.clientes.entity.ClienteDireccionEntity;
import com.bambino.clientes.entity.ClientePerfilEntity;
import com.bambino.clientes.entity.DocTipo;
import com.bambino.clientes.repository.ClienteDireccionRepository;
import com.bambino.clientes.repository.ClientePerfilRepository;
import com.bambino.delivery.dto.CoberturaDeliveryResponse;
import com.bambino.delivery.service.CoberturaDeliveryService;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
/**
 * Clase que maneja la funcionalidad de CarritoClienteService.
 */
public class CarritoClienteService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final OfertaProductoRepository ofertaProductoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClientePerfilRepository clientePerfilRepository;
    private final ClienteDireccionRepository clienteDireccionRepository;
    private final ConfiguracionGlobalRepository configuracionGlobalRepository;
    private final ZonaDeliveryRepository zonaDeliveryRepository;
    private final CoberturaDeliveryService coberturaDeliveryService;
    private final CarritoMapper carritoMapper;

    public CarritoClienteService(CarritoRepository carritoRepository,
                                 CarritoItemRepository carritoItemRepository,
                                 ProductoRepository productoRepository,
                                 OfertaProductoRepository ofertaProductoRepository,
                                 UsuarioRepository usuarioRepository,
                                 ClientePerfilRepository clientePerfilRepository,
                                 ClienteDireccionRepository clienteDireccionRepository,
                                 ConfiguracionGlobalRepository configuracionGlobalRepository,
                                 ZonaDeliveryRepository zonaDeliveryRepository,
                                 CoberturaDeliveryService coberturaDeliveryService,
                                 CarritoMapper carritoMapper) {
        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.productoRepository = productoRepository;
        this.ofertaProductoRepository = ofertaProductoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clientePerfilRepository = clientePerfilRepository;
        this.clienteDireccionRepository = clienteDireccionRepository;
        this.configuracionGlobalRepository = configuracionGlobalRepository;
        this.zonaDeliveryRepository = zonaDeliveryRepository;
        this.coberturaDeliveryService = coberturaDeliveryService;
        this.carritoMapper = carritoMapper;
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de verCarrito.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CarritoResumenResponse verCarrito(String emailAutenticado) {
        CarritoEntity carrito = obtenerOCrearCarritoAbierto(emailAutenticado);
        return construirResumen(carrito, "RECOJO");
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de agregarItem.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CarritoResumenResponse agregarItem(String emailAutenticado, CarritoItemAgregarRequest request) {
        return agregarItems(emailAutenticado, new CarritoItemsAgregarRequest(List.of(request)));
    }

    @Transactional
    public CarritoResumenResponse agregarItems(String emailAutenticado, CarritoItemsAgregarRequest request) {
        CarritoEntity carrito = obtenerOCrearCarritoAbierto(emailAutenticado);
        LocalDateTime ahora = LocalDateTime.now();

        for (CarritoItemAgregarRequest itemRequest : request.items()) {
            agregarItemEnCarrito(carrito, itemRequest, ahora);
        }

        actualizarTrazabilidadCarrito(carrito);
        return construirResumen(carrito, "RECOJO");
    }

    private void agregarItemEnCarrito(CarritoEntity carrito, CarritoItemAgregarRequest request, LocalDateTime ahora) {
        ProductoEntity producto = obtenerProductoValido(request.idProducto());

        CarritoItemEntity item = carritoItemRepository.findByIdCarritoAndIdProducto(carrito.getIdCarrito(), request.idProducto())
            .orElseGet(CarritoItemEntity::new);

        if (item.getIdCarritoItem() == null) {
            item.setIdCarrito(carrito.getIdCarrito());
            item.setIdProducto(producto.getIdProducto());
            item.setFechaCreacion(ahora);
            item.setDescuentoUnitarioSnapshot(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }

        item.setCantidad(normalizarCantidad(request.cantidad()));
        PrecioOfertaAplicada precioOferta = calcularPrecioOferta(producto);
        item.setPrecioUnitarioSnapshot(precioOferta.precioUnitario());
        item.setDescuentoUnitarioSnapshot(precioOferta.descuentoUnitario());
        item.setObservacion(limpiarTexto(request.observacion()));
        item.setFechaActualizacion(ahora);

        carritoItemRepository.save(item);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarItem.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idCarritoItem parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CarritoResumenResponse actualizarItem(String emailAutenticado, Long idCarritoItem, CarritoItemActualizarRequest request) {
        CarritoEntity carrito = obtenerOCrearCarritoAbierto(emailAutenticado);

        CarritoItemEntity item = carritoItemRepository.findByIdCarritoItemAndIdCarrito(idCarritoItem, carrito.getIdCarrito())
            .orElseThrow(() -> new NegocioException("item no existe en el carrito del cliente"));

        ProductoEntity producto = obtenerProductoValido(item.getIdProducto());

        item.setCantidad(normalizarCantidad(request.cantidad()));
        PrecioOfertaAplicada precioOferta = calcularPrecioOferta(producto);
        item.setPrecioUnitarioSnapshot(precioOferta.precioUnitario());
        item.setDescuentoUnitarioSnapshot(precioOferta.descuentoUnitario());
        item.setObservacion(limpiarTexto(request.observacion()));
        item.setFechaActualizacion(LocalDateTime.now());
        carritoItemRepository.save(item);

        actualizarTrazabilidadCarrito(carrito);
        return construirResumen(carrito, "RECOJO");
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de quitarItem.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idCarritoItem parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CarritoResumenResponse quitarItem(String emailAutenticado, Long idCarritoItem) {
        CarritoEntity carrito = obtenerOCrearCarritoAbierto(emailAutenticado);

        int filasEliminadas = carritoItemRepository.deleteByIdCarritoItemAndIdCarrito(idCarritoItem, carrito.getIdCarrito());
        if (filasEliminadas == 0) {
            throw new NegocioException("item no existe en el carrito del cliente");
        }

        actualizarTrazabilidadCarrito(carrito);
        return construirResumen(carrito, "RECOJO");
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de vaciarCarrito.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CarritoResumenResponse vaciarCarrito(String emailAutenticado) {
        CarritoEntity carrito = obtenerOCrearCarritoAbierto(emailAutenticado);
        carritoItemRepository.deleteByIdCarrito(carrito.getIdCarrito());
        actualizarTrazabilidadCarrito(carrito);
        return construirResumen(carrito, "RECOJO");
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de validarCheckout.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CheckoutValidarResponse validarCheckout(String emailAutenticado, CheckoutValidarRequest request) {
        String modalidad = request.modalidad().trim().toUpperCase(Locale.ROOT);
        String tipoComprobante = request.tipoComprobante().trim().toUpperCase(Locale.ROOT);

        CarritoEntity carrito = obtenerOCrearCarritoAbierto(emailAutenticado);
        CarritoResumenResponse resumen = construirResumen(carrito, modalidad, request.idDireccion());

        if (resumen.items().isEmpty()) {
            throw new NegocioException("el carrito esta vacio");
        }

        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        ClientePerfilEntity perfil = clientePerfilRepository.findById(idCliente)
            .orElseThrow(() -> new NegocioException("perfil de cliente no encontrado"));

        validarModalidad(idCliente, modalidad, request.idDireccion(), resumen.total());
        validarComprobante(perfil, tipoComprobante, request.docNumero(), request.razonSocial(), request.direccionFiscal());

        return new CheckoutValidarResponse(
            true,
            "checkout valido para continuar con pago",
            modalidad,
            tipoComprobante,
            resumen.subtotal(),
            resumen.descuentoTotal(),
            resumen.impuestoTotal(),
            resumen.costoDelivery(),
            resumen.total()
        );
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de confirmarCheckout.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CheckoutValidarResponse confirmarCheckout(String emailAutenticado, CheckoutValidarRequest request) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        CarritoEntity carrito = carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(idCliente, EstadoCarrito.ABIERTO)
            .or(() -> carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(idCliente, EstadoCarrito.CONFIRMADO))
            .orElseGet(() -> carritoRepository.findFirstByIdClienteOrderByIdCarritoDesc(idCliente)
                .orElseThrow(() -> new NegocioException("no existe carrito para confirmar")));

        if (carrito.getEstado() == EstadoCarrito.CONFIRMADO) {
            CarritoResumenResponse resumenConfirmado = construirResumen(carrito, request.modalidad().trim().toUpperCase(Locale.ROOT), request.idDireccion());
            return new CheckoutValidarResponse(
                true,
                "checkout ya confirmado previamente",
                request.modalidad().trim().toUpperCase(Locale.ROOT),
                request.tipoComprobante().trim().toUpperCase(Locale.ROOT),
                resumenConfirmado.subtotal(),
                resumenConfirmado.descuentoTotal(),
                resumenConfirmado.impuestoTotal(),
                resumenConfirmado.costoDelivery(),
                resumenConfirmado.total()
            );
        }

        if (carrito.getEstado() != EstadoCarrito.ABIERTO) {
            throw new NegocioException("no existe carrito abierto para confirmar checkout");
        }

        CheckoutValidarResponse validacion = validarCheckout(emailAutenticado, request);

        carrito.setEstado(EstadoCarrito.CONFIRMADO);
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);

        return new CheckoutValidarResponse(
            true,
            "checkout confirmado, continuar con modulo de pago",
            validacion.modalidad(),
            validacion.tipoComprobante(),
            validacion.subtotal(),
            validacion.descuentoTotal(),
            validacion.impuestoTotal(),
            validacion.costoDelivery(),
            validacion.total()
        );
    }

    private void validarModalidad(Long idCliente, String modalidad, Long idDireccion, BigDecimal totalConImpuestoSinDelivery) {
        if (!"RECOJO".equals(modalidad) && !"DELIVERY".equals(modalidad)) {
            throw new NegocioException("modalidad no valida, use RECOJO o DELIVERY");
        }

        if ("DELIVERY".equals(modalidad)) {
            ClienteDireccionEntity direccion;
            if (idDireccion == null) {
                direccion = clienteDireccionRepository.findFirstByIdClienteAndActivoTrueAndEsPrincipalTrue(idCliente)
                    .orElseThrow(() -> new NegocioException("no tiene direccion principal, registre una direccion o envie idDireccion"));
            } else {
                direccion = clienteDireccionRepository.findByIdDireccionAndIdClienteAndActivoTrue(idDireccion, idCliente)
                    .orElseThrow(() -> new NegocioException("la direccion no existe o no pertenece al cliente"));
            }

            if (direccion.getLatitud() == null || direccion.getLongitud() == null) {
                throw new NegocioException("la direccion delivery debe tener coordenadas de mapa");
            }

            CoberturaDeliveryResponse cobertura = coberturaDeliveryService.evaluarCobertura(direccion.getLatitud(), direccion.getLongitud());
            if (!cobertura.dentroCobertura()) {
                throw new NegocioException("direccion fuera de la cobertura de delivery (radio " + cobertura.radioKm().stripTrailingZeros().toPlainString() + " km)");
            }

            ZonaDeliveryEntity zonaAplicada = cobertura.idZona() == null ? null :
                zonaDeliveryRepository.findById(cobertura.idZona()).orElse(null);
            if (zonaAplicada == null) {
                throw new NegocioException("no se encontro zona delivery aplicable para la direccion");
            }
            validarHorarioDeliveryZona(zonaAplicada);

            ConfiguracionGlobalEntity config = obtenerConfiguracionGlobal();
            BigDecimal minimoGlobal = nvl(config.getDeliveryMontoMinimo());
            BigDecimal minimoZona = nvl(zonaAplicada.getMontoMinimo());
            BigDecimal minimoAplicable = minimoGlobal.max(minimoZona);
            if (totalConImpuestoSinDelivery.compareTo(minimoAplicable) < 0) {
                throw new NegocioException("no cumple el monto minimo para delivery");
            }
        }
    }

    private void validarHorarioDeliveryZona(ZonaDeliveryEntity zona) {
        LocalTime ahoraLima = LocalTime.now(ZoneId.of("America/Lima"));
        LocalTime inicio = zona.getHoraInicioAtencion();
        LocalTime fin = zona.getHoraFinAtencion();
        if (inicio != null && fin != null && (ahoraLima.isBefore(inicio) || ahoraLima.isAfter(fin))) {
            throw new NegocioException("delivery fuera de horario de atencion de zona");
        }
    }

    private void validarComprobante(ClientePerfilEntity perfil,
                                    String tipoComprobante,
                                    String docNumeroRequest,
                                    String razonSocial,
                                    String direccionFiscal) {
        if (!"BOLETA".equals(tipoComprobante) && !"FACTURA".equals(tipoComprobante)) {
            throw new NegocioException("tipoComprobante no valido, use BOLETA o FACTURA");
        }

        String docNumero = limpiarTexto(docNumeroRequest);
        if (docNumero == null) {
            docNumero = limpiarTexto(perfil.getDocNumero());
        }

        if ("BOLETA".equals(tipoComprobante)) {
            if (docNumero == null || docNumero.isBlank()) {
                throw new NegocioException("boleta requiere documento del cliente");
            }

            if (perfil.getDocTipo() == DocTipo.DNI && !esNumero(docNumero, 8)) {
                throw new NegocioException("boleta con DNI requiere 8 digitos");
            }

            return;
        }

        if (docNumero == null || !esNumero(docNumero, 11)) {
            throw new NegocioException("factura requiere RUC valido de 11 digitos");
        }

        if (perfil.getDocTipo() != DocTipo.RUC && docNumeroRequest == null) {
            throw new NegocioException("para factura debe enviar RUC explicito");
        }
    }

    private CarritoResumenResponse construirResumen(CarritoEntity carrito, String modalidad) {
        return construirResumen(carrito, modalidad, null);
    }

    private CarritoResumenResponse construirResumen(CarritoEntity carrito, String modalidad, Long idDireccion) {
        List<CarritoItemEntity> items = carritoItemRepository.findByIdCarritoOrderByIdCarritoItemAsc(carrito.getIdCarrito());

        Map<Long, ProductoEntity> productos = productoRepository.findAllById(
                items.stream().map(CarritoItemEntity::getIdProducto).distinct().toList())
            .stream()
            .collect(Collectors.toMap(ProductoEntity::getIdProducto, Function.identity()));

        List<CarritoItemResponse> detalle = new ArrayList<>();

        BigDecimal subtotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal descuentoTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalItems = BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);

        for (CarritoItemEntity item : items) {
            ProductoEntity producto = productos.get(item.getIdProducto());
            CarritoItemResponse dto = carritoMapper.toItemResponse(item, producto);
            detalle.add(dto);

            BigDecimal subtotalLineaBruto = item.getPrecioUnitarioSnapshot().multiply(item.getCantidad());
            BigDecimal descuentoLinea = item.getDescuentoUnitarioSnapshot().multiply(item.getCantidad());

            subtotal = subtotal.add(subtotalLineaBruto).setScale(2, RoundingMode.HALF_UP);
            descuentoTotal = descuentoTotal.add(descuentoLinea).setScale(2, RoundingMode.HALF_UP);
            totalItems = totalItems.add(item.getCantidad()).setScale(3, RoundingMode.HALF_UP);
        }

        ConfiguracionGlobalEntity config = obtenerConfiguracionGlobal();
        BigDecimal baseGravada = subtotal.subtract(descuentoTotal).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal impuestoTotal = baseGravada
            .multiply(nvl(config.getIgvPorcentaje()))
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal costoDelivery = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        if ("DELIVERY".equals(modalidad)) {
            ClienteDireccionEntity direccionPrincipal = obtenerDireccionParaDelivery(carrito.getIdCliente(), idDireccion);
            if (direccionPrincipal != null && direccionPrincipal.getLatitud() != null && direccionPrincipal.getLongitud() != null) {
                CoberturaDeliveryResponse cobertura = coberturaDeliveryService.evaluarCobertura(
                    direccionPrincipal.getLatitud(), direccionPrincipal.getLongitud()
                );
                if (cobertura.idZona() != null && cobertura.tarifaBase() != null) {
                    costoDelivery = cobertura.tarifaBase().setScale(2, RoundingMode.HALF_UP);
                }
            }
        }

        BigDecimal total = baseGravada.add(impuestoTotal).add(costoDelivery).setScale(2, RoundingMode.HALF_UP);

        return new CarritoResumenResponse(
            carrito.getIdCarrito(),
            carrito.getEstado().name(),
            subtotal,
            descuentoTotal,
            impuestoTotal,
            costoDelivery,
            total,
            totalItems,
            detalle
        );
    }

    private ClienteDireccionEntity obtenerDireccionParaDelivery(Long idCliente, Long idDireccion) {
        if (idDireccion != null) {
            return clienteDireccionRepository.findByIdDireccionAndIdClienteAndActivoTrue(idDireccion, idCliente)
                .orElse(null);
        }
        return clienteDireccionRepository
            .findFirstByIdClienteAndActivoTrueAndEsPrincipalTrue(idCliente)
            .orElse(null);
    }

    private CarritoEntity obtenerOCrearCarritoAbierto(String emailAutenticado) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);

        return carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(idCliente, EstadoCarrito.ABIERTO)
            .orElseGet(() -> crearCarritoAbierto(idCliente));
    }

    private CarritoEntity crearCarritoAbierto(Long idCliente) {
        LocalDateTime ahora = LocalDateTime.now();

        CarritoEntity carrito = new CarritoEntity();
        carrito.setIdCliente(idCliente);
        carrito.setEstado(EstadoCarrito.ABIERTO);
        carrito.setCanal(CanalCarrito.WEB);
        carrito.setUsuarioCreacion(idCliente);
        carrito.setUsuarioActualizacion(idCliente);
        carrito.setFechaCreacion(ahora);
        carrito.setFechaActualizacion(ahora);

        return carritoRepository.save(carrito);
    }

    private ProductoEntity obtenerProductoValido(Long idProducto) {
        ProductoEntity producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new NegocioException("producto no existe"));

        if (!Boolean.TRUE.equals(producto.getVisibleWeb()) || !Boolean.TRUE.equals(producto.getDisponible()) || producto.getEstado() != EstadoProducto.ACTIVO) {
            throw new NegocioException("producto no disponible para carrito");
        }

        return producto;
    }

    private void actualizarTrazabilidadCarrito(CarritoEntity carrito) {
        carrito.setUsuarioActualizacion(carrito.getIdCliente());
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }

    private Long obtenerIdClientePorEmail(String emailAutenticado) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));
        return usuario.getIdUsuario();
    }

    private BigDecimal normalizarCantidad(BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("cantidad debe ser mayor a 0");
        }
        return cantidad.setScale(3, RoundingMode.HALF_UP);
    }

    private ConfiguracionGlobalEntity obtenerConfiguracionGlobal() {
        return configuracionGlobalRepository.findById((short) 1)
            .orElseThrow(() -> new NegocioException("configuracion global no inicializada"));
    }

    private boolean esNumero(String valor, int longitud) {
        return valor != null && valor.length() == longitud && valor.chars().allMatch(Character::isDigit);
    }

    private BigDecimal nvl(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : valor.setScale(2, RoundingMode.HALF_UP);
    }

    private PrecioOfertaAplicada calcularPrecioOferta(ProductoEntity producto) {
        BigDecimal precioBase = producto.getPrecioBase().setScale(2, RoundingMode.HALF_UP);
        List<OfertaProductoEntity> ofertas = ofertaProductoRepository.findOfertasActivasByProducto(producto.getIdProducto(), LocalDateTime.now());
        if (ofertas.isEmpty()) {
            return new PrecioOfertaAplicada(precioBase, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }

        OfertaEntity oferta = ofertas.get(0).getOferta();
        BigDecimal descuentoUnitario = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        if (oferta.getTipo() == TipoOferta.PORCENTAJE && oferta.getValorDescuento() != null) {
            descuentoUnitario = precioBase
                .multiply(oferta.getValorDescuento())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (oferta.getTipo() == TipoOferta.MONTO_FIJO && oferta.getValorDescuento() != null) {
            descuentoUnitario = oferta.getValorDescuento().setScale(2, RoundingMode.HALF_UP);
        } else if (oferta.getTipo() == TipoOferta.PRECIO_ESPECIAL && oferta.getPrecioEspecial() != null) {
            BigDecimal precioEspecial = oferta.getPrecioEspecial().setScale(2, RoundingMode.HALF_UP);
            descuentoUnitario = precioBase.subtract(precioEspecial).setScale(2, RoundingMode.HALF_UP);
        } else if (oferta.getTipo() == TipoOferta.COMBO && oferta.getPrecioEspecial() != null) {
            BigDecimal precioEspecial = oferta.getPrecioEspecial().setScale(2, RoundingMode.HALF_UP);
            descuentoUnitario = precioBase.subtract(precioEspecial).setScale(2, RoundingMode.HALF_UP);
        }

        if (descuentoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            descuentoUnitario = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        if (descuentoUnitario.compareTo(precioBase) > 0) {
            descuentoUnitario = precioBase;
        }
        return new PrecioOfertaAplicada(precioBase, descuentoUnitario);
    }

    private record PrecioOfertaAplicada(BigDecimal precioUnitario, BigDecimal descuentoUnitario) {}

    private String limpiarTexto(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
