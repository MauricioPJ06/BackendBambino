package com.bambino.pagos.service;

import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.carrito.dto.CheckoutValidarResponse;
import com.bambino.carrito.service.CarritoClienteService;
import com.bambino.comprobantes.service.ComprobanteService;
import com.bambino.pagos.dto.PagoCheckoutConfirmarRequest;
import com.bambino.pagos.dto.PagoCheckoutOrdenResponse;
import com.bambino.pagos.dto.PagoCheckoutResponse;
import com.bambino.pagos.entity.EstadoPago;
import com.bambino.pagos.entity.MetodoPago;
import com.bambino.pagos.entity.PagoEntity;
import com.bambino.pagos.repository.PagoRepository;
import com.bambino.pagos.service.culqi.CulqiChargeClient;
import com.bambino.pagos.service.culqi.CulqiChargeResponse;
import com.bambino.pagos.service.culqi.CulqiOrderClient;
import com.bambino.pagos.service.culqi.CulqiOrderResponse;
import com.bambino.pagos.service.culqi.CulqiProperties;
import com.bambino.pedidos.dto.PedidoResponse;
import com.bambino.pedidos.entity.EstadoPedido;
import com.bambino.pedidos.entity.PedidoEntity;
import com.bambino.pedidos.entity.PedidoEstadoTransicionPermitidaEntity;
import com.bambino.pedidos.repository.PedidoEstadoHistorialRepository;
import com.bambino.pedidos.repository.PedidoEstadoTransicionPermitidaRepository;
import com.bambino.pedidos.repository.PedidoRepository;
import com.bambino.pedidos.service.PedidoEstadoService;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

class PagoServiceCheckoutTest {

    @Test
    void confirmarPagoCheckoutCreaPedidoFormalYRegistraPagoAprobado() {
        PagoRepository pagoRepository = mock(PagoRepository.class);
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        PedidoEstadoTransicionPermitidaRepository transicionRepository = mock(PedidoEstadoTransicionPermitidaRepository.class);
        PedidoEstadoHistorialRepository historialRepository = mock(PedidoEstadoHistorialRepository.class);
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        ComprobanteService comprobanteService = mock(ComprobanteService.class);
        AuditoriaService auditoriaService = mock(AuditoriaService.class);
        CarritoClienteService carritoClienteService = mock(CarritoClienteService.class);
        PedidoEstadoService pedidoEstadoService = mock(PedidoEstadoService.class);
        CulqiChargeClient culqiChargeClient = mock(CulqiChargeClient.class);
        CulqiOrderClient culqiOrderClient = mock(CulqiOrderClient.class);
        CulqiProperties culqiProperties = new CulqiProperties(
            "pk_test_xxx",
            "sk_test_xxx",
            "https://api.culqi.com",
            "https://js.culqi.com/checkout-js",
            "PEN"
        );

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(7L);
        usuario.setNombres("Fabrizio");
        usuario.setApellidos("Cliente");
        usuario.setTelefono("999888777");
        when(usuarioRepository.findByEmail("cliente@test.com")).thenReturn(Optional.of(usuario));
        when(pagoRepository.findByIdempotencyKey("checkout-123")).thenReturn(Optional.empty());
        when(carritoClienteService.confirmarCheckout(eq("cliente@test.com"), any())).thenReturn(new CheckoutValidarResponse(
            true,
            "checkout confirmado",
            "RECOJO",
            "BOLETA",
            new BigDecimal("50.00"),
            BigDecimal.ZERO,
            new BigDecimal("9.00"),
            BigDecimal.ZERO,
            new BigDecimal("59.00")
        ));
        when(pedidoEstadoService.crearPedidoDesdeCheckout(eq("cliente@test.com"), any())).thenReturn(new PedidoResponse(
            11L,
            "PED-000000011",
            EstadoPedido.CREADO.name(),
            "RECOJO",
            "BOLETA",
            new BigDecimal("50.00"),
            BigDecimal.ZERO,
            new BigDecimal("9.00"),
            new BigDecimal("59.00"),
            LocalDateTime.now()
        ));
        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setIdPedido(11L);
        pedidoEntity.setIdCliente(7L);
        pedidoEntity.setEstadoActual(EstadoPedido.CREADO);
        when(pedidoRepository.findById(11L)).thenReturn(Optional.of(pedidoEntity));
        when(transicionRepository.findByEstadoOrigenAndEstadoDestinoAndActorTipoAndActivoTrue(any(), any(), any()))
            .thenReturn(Optional.of(new PedidoEstadoTransicionPermitidaEntity()));
        when(pedidoEstadoService.obtenerPedidoCliente("cliente@test.com", 11L)).thenReturn(new PedidoResponse(
            11L,
            "PED-000000011",
            EstadoPedido.PAGO_APROBADO.name(),
            "RECOJO",
            "BOLETA",
            new BigDecimal("50.00"),
            BigDecimal.ZERO,
            new BigDecimal("9.00"),
            new BigDecimal("59.00"),
            LocalDateTime.now()
        ));
        when(pagoRepository.save(any(PagoEntity.class))).thenAnswer((invocation) -> {
            PagoEntity pago = invocation.getArgument(0);
            pago.setIdPago(22L);
            return pago;
        });
        when(culqiChargeClient.crearCargo(any())).thenReturn(new CulqiChargeResponse(
            "chr_test_123",
            "charge",
            "venta aprobada",
            null
        ));

        PagoService service = new PagoService(
            pagoRepository,
            pedidoRepository,
            transicionRepository,
            historialRepository,
            usuarioRepository,
            comprobanteService,
            auditoriaService,
            carritoClienteService,
            pedidoEstadoService,
            culqiChargeClient,
            culqiOrderClient,
            culqiProperties
        );

        PagoCheckoutResponse response = service.confirmarPagoCheckoutCliente("cliente@test.com", new PagoCheckoutConfirmarRequest(
            "RECOJO",
            "BOLETA",
            null,
            "12345678",
            null,
            null,
            MetodoPago.YAPE.name(),
            "checkout-123",
            "CULQI",
            "ype_test_123"
        ));

        assertThat(response.pedido().idPedido()).isEqualTo(11L);
        assertThat(response.pedido().estadoActual()).isEqualTo(EstadoPedido.PAGO_APROBADO.name());
        assertThat(response.pago().idPedido()).isEqualTo(11L);
        assertThat(response.pago().estado()).isEqualTo(EstadoPago.APROBADO.name());
        assertThat(response.pago().monto()).isEqualByComparingTo("59.00");
        assertThat(response.pago().proveedor()).isEqualTo("CULQI");
        assertThat(response.pago().proveedorTxnId()).isEqualTo("chr_test_123");
        verify(carritoClienteService).confirmarCheckout(eq("cliente@test.com"), any());
        verify(pedidoEstadoService).crearPedidoDesdeCheckout(eq("cliente@test.com"), any());
        verify(culqiChargeClient).crearCargo(any());
        verify(comprobanteService).emitirSiNoExiste(11L, 7L);
        verify(comprobanteService).enviarCorreoPorPedidoSiPendiente(11L, 7L);
        verify(pedidoRepository, times(2)).flush();
    }

    @Test
    void crearOrdenCulqiCheckoutConfirmaCarritoYDevuelveOrderId() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        CarritoClienteService carritoClienteService = mock(CarritoClienteService.class);
        CulqiOrderClient culqiOrderClient = mock(CulqiOrderClient.class);
        CulqiProperties culqiProperties = new CulqiProperties(
            "pk_test_xxx",
            "sk_test_xxx",
            "https://api.culqi.com",
            "https://js.culqi.com/checkout-js",
            "PEN"
        );

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(7L);
        usuario.setNombres("Fabrizio");
        usuario.setApellidos("Cliente");
        usuario.setTelefono("999888777");
        when(usuarioRepository.findByEmail("cliente@test.com")).thenReturn(Optional.of(usuario));
        when(carritoClienteService.confirmarCheckout(eq("cliente@test.com"), any())).thenReturn(new CheckoutValidarResponse(
            true,
            "checkout confirmado",
            "RECOJO",
            "BOLETA",
            new BigDecimal("50.00"),
            BigDecimal.ZERO,
            new BigDecimal("9.00"),
            BigDecimal.ZERO,
            new BigDecimal("59.00")
        ));
        when(culqiOrderClient.crearOrden(any())).thenReturn(new CulqiOrderResponse(
            "ord_test_123",
            "order",
            "pending"
        ));

        PagoService service = new PagoService(
            mock(PagoRepository.class),
            mock(PedidoRepository.class),
            mock(PedidoEstadoTransicionPermitidaRepository.class),
            mock(PedidoEstadoHistorialRepository.class),
            usuarioRepository,
            mock(ComprobanteService.class),
            mock(AuditoriaService.class),
            carritoClienteService,
            mock(PedidoEstadoService.class),
            mock(CulqiChargeClient.class),
            culqiOrderClient,
            culqiProperties
        );

        PagoCheckoutOrdenResponse response = service.crearOrdenCulqiCheckoutCliente("cliente@test.com", new com.bambino.carrito.dto.CheckoutValidarRequest(
            "RECOJO",
            "BOLETA",
            null,
            "12345678",
            null,
            null
        ));

        assertThat(response.orderId()).isEqualTo("ord_test_123");
        assertThat(response.total()).isEqualByComparingTo("59.00");
        assertThat(response.currency()).isEqualTo("PEN");
        verify(carritoClienteService).confirmarCheckout(eq("cliente@test.com"), any());
        ArgumentCaptor<com.bambino.pagos.service.culqi.CulqiOrderRequest> orderCaptor =
            ArgumentCaptor.forClass(com.bambino.pagos.service.culqi.CulqiOrderRequest.class);
        verify(culqiOrderClient).crearOrden(orderCaptor.capture());
        assertThat(orderCaptor.getValue().firstName()).isEqualTo("Fabrizio");
        assertThat(orderCaptor.getValue().lastName()).isEqualTo("Cliente");
        assertThat(orderCaptor.getValue().phoneNumber()).isEqualTo("900000001");
    }

    @Test
    void crearOrdenCulqiCheckoutRechazaMontoMenorAlMinimoDeCulqi() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        CarritoClienteService carritoClienteService = mock(CarritoClienteService.class);
        CulqiOrderClient culqiOrderClient = mock(CulqiOrderClient.class);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(7L);
        usuario.setNombres("Fabrizio");
        usuario.setApellidos("Cliente");
        when(usuarioRepository.findByEmail("cliente@test.com")).thenReturn(Optional.of(usuario));
        when(carritoClienteService.confirmarCheckout(eq("cliente@test.com"), any())).thenReturn(new CheckoutValidarResponse(
            true,
            "checkout confirmado",
            "RECOJO",
            "BOLETA",
            new BigDecimal("0.10"),
            BigDecimal.ZERO,
            new BigDecimal("0.02"),
            BigDecimal.ZERO,
            new BigDecimal("0.12")
        ));

        PagoService service = new PagoService(
            mock(PagoRepository.class),
            mock(PedidoRepository.class),
            mock(PedidoEstadoTransicionPermitidaRepository.class),
            mock(PedidoEstadoHistorialRepository.class),
            usuarioRepository,
            mock(ComprobanteService.class),
            mock(AuditoriaService.class),
            carritoClienteService,
            mock(PedidoEstadoService.class),
            mock(CulqiChargeClient.class),
            culqiOrderClient,
            new CulqiProperties("pk_test_xxx", "sk_test_xxx", "https://api.culqi.com", "https://js.culqi.com/checkout-js", "PEN")
        );

        assertThatThrownBy(() -> service.crearOrdenCulqiCheckoutCliente("cliente@test.com", new com.bambino.carrito.dto.CheckoutValidarRequest(
            "RECOJO",
            "BOLETA",
            null,
            "12345678",
            null,
            null
        ))).hasMessageContaining("monto minimo para pagar con Culqi es S/ 6.00");

        verify(culqiOrderClient, never()).crearOrden(any());
    }

    @Test
    void confirmarPagoCheckoutConCulqiRequiereToken() {
        PagoService service = new PagoService(
            mock(PagoRepository.class),
            mock(PedidoRepository.class),
            mock(PedidoEstadoTransicionPermitidaRepository.class),
            mock(PedidoEstadoHistorialRepository.class),
            mock(UsuarioRepository.class),
            mock(ComprobanteService.class),
            mock(AuditoriaService.class),
            mock(CarritoClienteService.class),
            mock(PedidoEstadoService.class),
            mock(CulqiChargeClient.class),
            mock(CulqiOrderClient.class),
            new CulqiProperties("pk_test_xxx", "sk_test_xxx", "https://api.culqi.com", "https://js.culqi.com/checkout-js", "PEN")
        );

        assertThatThrownBy(() -> service.confirmarPagoCheckoutCliente("cliente@test.com", new PagoCheckoutConfirmarRequest(
            "RECOJO",
            "BOLETA",
            null,
            "12345678",
            null,
            null,
            MetodoPago.TARJETA.name(),
            "checkout-123",
            "CULQI",
            null
        ))).hasMessageContaining("token Culqi");
    }
}
