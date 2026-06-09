package com.bambino.carrito.service;

import com.bambino.carrito.mapper.CarritoMapper;
import com.bambino.carrito.dto.CarritoItemResponse;
import com.bambino.carrito.dto.CarritoItemAgregarRequest;
import com.bambino.carrito.dto.CarritoItemsAgregarRequest;
import com.bambino.carrito.dto.CarritoResumenResponse;
import com.bambino.carrito.entity.CarritoEntity;
import com.bambino.carrito.entity.CarritoItemEntity;
import com.bambino.carrito.entity.ConfiguracionGlobalEntity;
import com.bambino.carrito.entity.EstadoCarrito;
import com.bambino.carrito.repository.CarritoItemRepository;
import com.bambino.carrito.repository.CarritoRepository;
import com.bambino.carrito.repository.ConfiguracionGlobalRepository;
import com.bambino.carrito.repository.ZonaDeliveryRepository;
import com.bambino.catalogo.entity.EstadoOferta;
import com.bambino.catalogo.entity.OfertaEntity;
import com.bambino.catalogo.entity.OfertaProductoEntity;
import com.bambino.catalogo.entity.ProductoEntity;
import com.bambino.catalogo.entity.TipoOferta;
import com.bambino.catalogo.repository.OfertaProductoRepository;
import com.bambino.catalogo.repository.ProductoRepository;
import com.bambino.clientes.entity.ClienteDireccionEntity;
import com.bambino.clientes.entity.ClientePerfilEntity;
import com.bambino.clientes.entity.DocTipo;
import com.bambino.clientes.repository.ClienteDireccionRepository;
import com.bambino.clientes.repository.ClientePerfilRepository;
import com.bambino.delivery.dto.CoberturaDeliveryResponse;
import com.bambino.delivery.service.CoberturaDeliveryService;
import com.bambino.carrito.dto.CheckoutValidarRequest;
import com.bambino.carrito.dto.CheckoutValidarResponse;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CarritoClienteServiceOfertaTest {

    @Mock private CarritoRepository carritoRepository;
    @Mock private CarritoItemRepository carritoItemRepository;
    @Mock private ProductoRepository productoRepository;
    @Mock private OfertaProductoRepository ofertaProductoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ClientePerfilRepository clientePerfilRepository;
    @Mock private ClienteDireccionRepository clienteDireccionRepository;
    @Mock private ConfiguracionGlobalRepository configuracionGlobalRepository;
    @Mock private ZonaDeliveryRepository zonaDeliveryRepository;
    @Mock private CoberturaDeliveryService coberturaDeliveryService;
    @Mock private CarritoMapper carritoMapper;

    @InjectMocks
    private CarritoClienteService carritoClienteService;

    @Test
    void calcularPrecioOferta_porcentaje_aplicaDescuentoCorrecto() {
        ProductoEntity producto = new ProductoEntity();
        producto.setIdProducto(10L);
        producto.setPrecioBase(new BigDecimal("100.00"));

        OfertaEntity oferta = new OfertaEntity();
        oferta.setTipo(TipoOferta.PORCENTAJE);
        oferta.setValorDescuento(new BigDecimal("20.00"));
        oferta.setEstado(EstadoOferta.ACTIVA);
        oferta.setFechaInicio(LocalDateTime.now().minusDays(1));
        oferta.setFechaFin(LocalDateTime.now().plusDays(1));

        OfertaProductoEntity enlace = new OfertaProductoEntity();
        enlace.setOferta(oferta);
        enlace.setProducto(producto);

        when(ofertaProductoRepository.findOfertasActivasByProducto(eq(10L), any(LocalDateTime.class)))
            .thenReturn(List.of(enlace));

        Object resultado = ReflectionTestUtils.invokeMethod(carritoClienteService, "calcularPrecioOferta", producto);

        BigDecimal precioUnitario = (BigDecimal) ReflectionTestUtils.invokeMethod(resultado, "precioUnitario");
        BigDecimal descuentoUnitario = (BigDecimal) ReflectionTestUtils.invokeMethod(resultado, "descuentoUnitario");

        assertEquals(new BigDecimal("100.00"), precioUnitario);
        assertEquals(new BigDecimal("20.00"), descuentoUnitario);
    }

    @Test
    void construirResumen_deliveryUsaDireccionSeleccionadaParaTarifa() {
        CarritoEntity carrito = new CarritoEntity();
        carrito.setIdCarrito(1L);
        carrito.setIdCliente(50L);
        carrito.setEstado(EstadoCarrito.ABIERTO);

        CarritoItemEntity item = new CarritoItemEntity();
        item.setIdCarritoItem(10L);
        item.setIdCarrito(1L);
        item.setIdProducto(20L);
        item.setCantidad(new BigDecimal("1.000"));
        item.setPrecioUnitarioSnapshot(new BigDecimal("100.00"));
        item.setDescuentoUnitarioSnapshot(new BigDecimal("0.00"));

        ProductoEntity producto = new ProductoEntity();
        producto.setIdProducto(20L);
        producto.setNombre("Producto");

        ConfiguracionGlobalEntity config = new ConfiguracionGlobalEntity();
        config.setIgvPorcentaje(new BigDecimal("18.00"));
        config.setDeliveryMontoMinimo(new BigDecimal("0.00"));

        ClienteDireccionEntity direccionSeleccionada = new ClienteDireccionEntity();
        direccionSeleccionada.setIdDireccion(99L);
        direccionSeleccionada.setLatitud(new BigDecimal("-12.1000000"));
        direccionSeleccionada.setLongitud(new BigDecimal("-77.0000000"));

        when(carritoItemRepository.findByIdCarritoOrderByIdCarritoItemAsc(1L)).thenReturn(List.of(item));
        when(productoRepository.findAllById(List.of(20L))).thenReturn(List.of(producto));
        when(carritoMapper.toItemResponse(item, producto)).thenReturn(new CarritoItemResponse(
            10L,
            20L,
            "Producto",
            new BigDecimal("1.000"),
            new BigDecimal("100.00"),
            new BigDecimal("0.00"),
            new BigDecimal("100.00"),
            null,
            null
        ));
        when(configuracionGlobalRepository.findById((short) 1)).thenReturn(Optional.of(config));
        when(clienteDireccionRepository.findByIdDireccionAndIdClienteAndActivoTrue(99L, 50L)).thenReturn(Optional.of(direccionSeleccionada));
        when(coberturaDeliveryService.evaluarCobertura(direccionSeleccionada.getLatitud(), direccionSeleccionada.getLongitud()))
            .thenReturn(new CoberturaDeliveryResponse(3L, true, new BigDecimal("1.00"), new BigDecimal("5.00"), new BigDecimal("7.50"), new BigDecimal("0.00"), "ok"));

        CarritoResumenResponse resumen = ReflectionTestUtils.invokeMethod(carritoClienteService, "construirResumen", carrito, "DELIVERY", 99L);

        assertEquals(new BigDecimal("7.50"), resumen.costoDelivery());
        assertEquals(new BigDecimal("125.50"), resumen.total());
    }

    @Test
    void confirmarCheckout_reutilizaCarritoConfirmadoCuandoNoHayAbierto() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(50L);

        CarritoEntity confirmado = new CarritoEntity();
        confirmado.setIdCarrito(1L);
        confirmado.setIdCliente(50L);
        confirmado.setEstado(EstadoCarrito.CONFIRMADO);

        CarritoItemEntity item = new CarritoItemEntity();
        item.setIdCarritoItem(10L);
        item.setIdCarrito(1L);
        item.setIdProducto(20L);
        item.setCantidad(new BigDecimal("1.000"));
        item.setPrecioUnitarioSnapshot(new BigDecimal("100.00"));
        item.setDescuentoUnitarioSnapshot(new BigDecimal("0.00"));

        ProductoEntity producto = new ProductoEntity();
        producto.setIdProducto(20L);
        producto.setNombre("Producto");

        ConfiguracionGlobalEntity config = new ConfiguracionGlobalEntity();
        config.setIgvPorcentaje(new BigDecimal("18.00"));

        ClientePerfilEntity perfil = new ClientePerfilEntity();
        perfil.setIdCliente(50L);
        perfil.setDocTipo(DocTipo.DNI);
        perfil.setDocNumero("12345678");

        when(usuarioRepository.findByEmail("cliente@test.com")).thenReturn(Optional.of(usuario));
        when(carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(50L, EstadoCarrito.ABIERTO)).thenReturn(Optional.empty());
        when(carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(50L, EstadoCarrito.CONFIRMADO)).thenReturn(Optional.of(confirmado));
        when(carritoItemRepository.findByIdCarritoOrderByIdCarritoItemAsc(1L)).thenReturn(List.of(item));
        when(productoRepository.findAllById(List.of(20L))).thenReturn(List.of(producto));
        when(carritoMapper.toItemResponse(item, producto)).thenReturn(new CarritoItemResponse(
            10L,
            20L,
            "Producto",
            new BigDecimal("1.000"),
            new BigDecimal("100.00"),
            new BigDecimal("0.00"),
            new BigDecimal("100.00"),
            null,
            null
        ));
        when(configuracionGlobalRepository.findById((short) 1)).thenReturn(Optional.of(config));

        CheckoutValidarResponse response = carritoClienteService.confirmarCheckout("cliente@test.com", new CheckoutValidarRequest(
            "RECOJO",
            "BOLETA",
            null,
            "12345678",
            null,
            null
        ));

        assertEquals("checkout ya confirmado previamente", response.mensaje());
        assertEquals(new BigDecimal("118.00"), response.total());
    }

    @Test
    void verCarrito_noReabreCarritoConfirmadoParaNuevaCompra() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(50L);

        CarritoEntity confirmado = new CarritoEntity();
        confirmado.setIdCarrito(1L);
        confirmado.setIdCliente(50L);
        confirmado.setEstado(EstadoCarrito.CONFIRMADO);

        ConfiguracionGlobalEntity config = new ConfiguracionGlobalEntity();
        config.setIgvPorcentaje(new BigDecimal("18.00"));

        when(usuarioRepository.findByEmail("cliente@test.com")).thenReturn(Optional.of(usuario));
        when(carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(50L, EstadoCarrito.ABIERTO)).thenReturn(Optional.empty());
        when(carritoRepository.save(any(CarritoEntity.class))).thenAnswer(invocation -> {
            CarritoEntity carrito = invocation.getArgument(0);
            carrito.setIdCarrito(2L);
            return carrito;
        });
        when(carritoItemRepository.findByIdCarritoOrderByIdCarritoItemAsc(2L)).thenReturn(List.of());
        when(productoRepository.findAllById(anyIterable())).thenReturn(List.of());
        when(configuracionGlobalRepository.findById((short) 1)).thenReturn(Optional.of(config));

        CarritoResumenResponse response = carritoClienteService.verCarrito("cliente@test.com");

        assertEquals(2L, response.idCarrito());
        assertEquals("ABIERTO", response.estado());
        assertEquals(0, response.items().size());
        assertEquals(EstadoCarrito.CONFIRMADO, confirmado.getEstado());
        verify(carritoRepository).save(any(CarritoEntity.class));
    }

    @Test
    void agregarItems_guardaVariosProductosYActualizaCarritoUnaVez() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(50L);

        CarritoEntity carrito = new CarritoEntity();
        carrito.setIdCarrito(1L);
        carrito.setIdCliente(50L);
        carrito.setEstado(EstadoCarrito.ABIERTO);

        ProductoEntity producto = new ProductoEntity();
        producto.setIdProducto(20L);
        producto.setNombre("Producto");
        producto.setVisibleWeb(true);
        producto.setDisponible(true);
        producto.setEstado(com.bambino.catalogo.entity.EstadoProducto.ACTIVO);
        producto.setPrecioBase(new BigDecimal("10.00"));

        ProductoEntity adicional = new ProductoEntity();
        adicional.setIdProducto(21L);
        adicional.setNombre("Adicional");
        adicional.setVisibleWeb(true);
        adicional.setDisponible(true);
        adicional.setEstado(com.bambino.catalogo.entity.EstadoProducto.ACTIVO);
        adicional.setPrecioBase(new BigDecimal("5.00"));

        ConfiguracionGlobalEntity config = new ConfiguracionGlobalEntity();
        config.setIgvPorcentaje(new BigDecimal("18.00"));

        when(usuarioRepository.findByEmail("cliente@test.com")).thenReturn(Optional.of(usuario));
        when(carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(50L, EstadoCarrito.ABIERTO)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(20L)).thenReturn(Optional.of(producto));
        when(productoRepository.findById(21L)).thenReturn(Optional.of(adicional));
        when(carritoItemRepository.findByIdCarritoAndIdProducto(1L, 20L)).thenReturn(Optional.empty());
        when(carritoItemRepository.findByIdCarritoAndIdProducto(1L, 21L)).thenReturn(Optional.empty());
        when(carritoItemRepository.save(any(CarritoItemEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(carritoRepository.save(any(CarritoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(carritoItemRepository.findByIdCarritoOrderByIdCarritoItemAsc(1L)).thenReturn(List.of());
        when(productoRepository.findAllById(anyIterable())).thenReturn(List.of());
        when(configuracionGlobalRepository.findById((short) 1)).thenReturn(Optional.of(config));

        carritoClienteService.agregarItems("cliente@test.com", new CarritoItemsAgregarRequest(List.of(
            new CarritoItemAgregarRequest(20L, BigDecimal.ONE, "Sin cremas"),
            new CarritoItemAgregarRequest(21L, new BigDecimal("2"), null)
        )));

        verify(carritoItemRepository, times(2)).save(any(CarritoItemEntity.class));
        verify(carritoRepository, times(1)).save(carrito);
    }

    @Test
    void quitarItem_eliminaConOperacionDirectaYDevuelveResumenActualizado() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(50L);

        CarritoEntity carrito = new CarritoEntity();
        carrito.setIdCarrito(1L);
        carrito.setIdCliente(50L);
        carrito.setEstado(EstadoCarrito.ABIERTO);

        CarritoItemEntity itemEliminado = new CarritoItemEntity();
        itemEliminado.setIdCarritoItem(42L);
        itemEliminado.setIdCarrito(1L);
        itemEliminado.setIdProducto(20L);

        ConfiguracionGlobalEntity config = new ConfiguracionGlobalEntity();
        config.setIgvPorcentaje(new BigDecimal("18.00"));

        when(usuarioRepository.findByEmail("cliente@test.com")).thenReturn(Optional.of(usuario));
        when(carritoRepository.findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(50L, EstadoCarrito.ABIERTO)).thenReturn(Optional.of(carrito));
        when(carritoItemRepository.deleteByIdCarritoItemAndIdCarrito(42L, 1L)).thenReturn(1);
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(carritoItemRepository.findByIdCarritoOrderByIdCarritoItemAsc(1L)).thenReturn(List.of());
        when(productoRepository.findAllById(anyIterable())).thenReturn(List.of());
        when(configuracionGlobalRepository.findById((short) 1)).thenReturn(Optional.of(config));

        CarritoResumenResponse response = carritoClienteService.quitarItem("cliente@test.com", 42L);

        assertEquals(0, response.items().size());
        assertEquals(new BigDecimal("0.00"), response.total());
        verify(carritoItemRepository, never()).delete(itemEliminado);
        verify(carritoItemRepository).deleteByIdCarritoItemAndIdCarrito(42L, 1L);
    }

    @Test
    void validarComprobante_facturaNoRequiereDatosFiscales() {
        ClientePerfilEntity perfil = new ClientePerfilEntity();
        perfil.setDocTipo(DocTipo.DNI);
        perfil.setDocNumero("12345678");

        assertDoesNotThrow(() -> ReflectionTestUtils.invokeMethod(
            carritoClienteService,
            "validarComprobante",
            perfil,
            "FACTURA",
            "20123456789",
            null,
            null
        ));
    }
}
