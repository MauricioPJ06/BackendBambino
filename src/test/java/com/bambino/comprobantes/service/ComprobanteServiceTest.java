package com.bambino.comprobantes.service;

import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.comprobantes.dto.ComprobanteResponse;
import com.bambino.comprobantes.entity.ComprobanteEntity;
import com.bambino.comprobantes.entity.EstadoComprobante;
import com.bambino.comprobantes.entity.EmpresaEntity;
import com.bambino.comprobantes.entity.SerieComprobanteEntity;
import com.bambino.comprobantes.entity.TipoComprobante;
import com.bambino.comprobantes.repository.ComprobanteDetalleRepository;
import com.bambino.comprobantes.repository.ComprobanteRepository;
import com.bambino.comprobantes.repository.EmpresaRepository;
import com.bambino.comprobantes.repository.SerieComprobanteRepository;
import com.bambino.pedidos.entity.DocReceptorPedido;
import com.bambino.pedidos.entity.PedidoEntity;
import com.bambino.pedidos.entity.PedidoItemEntity;
import com.bambino.pedidos.entity.TipoComprobantePedido;
import com.bambino.pedidos.repository.PedidoItemRepository;
import com.bambino.pedidos.repository.PedidoRepository;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.seguridad.entity.UsuarioEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ComprobanteServiceTest {

    @Test
    void emitirComprobanteUsaCorrelativoConNueveDigitos() {
        ComprobanteRepository comprobanteRepository = mock(ComprobanteRepository.class);
        ComprobanteDetalleRepository detalleRepository = mock(ComprobanteDetalleRepository.class);
        SerieComprobanteRepository serieRepository = mock(SerieComprobanteRepository.class);
        EmpresaRepository empresaRepository = mock(EmpresaRepository.class);
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        PedidoItemRepository pedidoItemRepository = mock(PedidoItemRepository.class);

        PedidoEntity pedido = new PedidoEntity();
        pedido.setIdPedido(11L);
        pedido.setTipoComprobante(TipoComprobantePedido.BOLETA);
        pedido.setDocReceptorTipo(DocReceptorPedido.DNI);
        pedido.setDocReceptorNumero("12345678");
        pedido.setRazonSocialSnapshot("Cliente Test");
        pedido.setSubtotal(new BigDecimal("10.00"));
        pedido.setImpuestoTotal(new BigDecimal("1.80"));
        pedido.setTotal(new BigDecimal("11.80"));

        EmpresaEntity empresa = new EmpresaEntity();
        empresa.setIdEmpresa(1L);

        SerieComprobanteEntity serie = new SerieComprobanteEntity();
        serie.setIdSerie(1L);
        serie.setIdEmpresa(1L);
        serie.setTipoComprobante(TipoComprobante.BOLETA);
        serie.setSerie("B001");
        serie.setCorrelativoActual(0L);

        PedidoItemEntity item = new PedidoItemEntity();
        item.setIdPedidoItem(21L);
        item.setIdPedido(11L);
        item.setNombreProductoSnapshot("1/4 de Pollo");
        item.setCantidad(BigDecimal.ONE);
        item.setPrecioUnitario(new BigDecimal("10.00"));
        item.setDescuentoUnitario(BigDecimal.ZERO);
        item.setSubtotalLinea(new BigDecimal("10.00"));
        item.setFechaCreacion(LocalDateTime.now());

        when(comprobanteRepository.findByIdPedido(11L)).thenReturn(Optional.empty());
        when(pedidoRepository.findById(11L)).thenReturn(Optional.of(pedido));
        when(empresaRepository.findFirstByActivoTrueOrderByIdEmpresaAsc()).thenReturn(Optional.of(empresa));
        when(serieRepository.findFirstByIdEmpresaAndTipoComprobanteAndActivoTrueOrderByIdSerieAsc(1L, TipoComprobante.BOLETA))
            .thenReturn(Optional.of(serie));
        when(comprobanteRepository.save(any(ComprobanteEntity.class))).thenAnswer(invocation -> {
            ComprobanteEntity comprobante = invocation.getArgument(0);
            comprobante.setIdComprobante(31L);
            return comprobante;
        });
        when(pedidoItemRepository.findByIdPedidoOrderByIdPedidoItemAsc(11L)).thenReturn(List.of(item));
        when(detalleRepository.findByIdComprobanteOrderByIdComprobanteDetalleAsc(31L)).thenReturn(List.of());

        ComprobanteService service = new ComprobanteService(
            comprobanteRepository,
            detalleRepository,
            serieRepository,
            empresaRepository,
            pedidoRepository,
            pedidoItemRepository,
            mock(UsuarioRepository.class),
            mock(ComprobanteCorreoService.class),
            mock(AuditoriaService.class)
        );

        ComprobanteResponse response = service.emitirSiNoExiste(11L, 7L);

        assertThat(response.correlativo()).isEqualTo(1L);
        assertThat(response.numeroCompleto()).isEqualTo("B001-000000001");
        assertThat(response.correoEnviado()).isFalse();
        assertThat(response.correoDestino()).isNull();
        assertThat(response.fechaCorreoEnvio()).isNull();
        assertThat(response.correoError()).isNull();
    }

    @Test
    void enviarCorreoAdminMarcaComprobanteComoEnviado() {
        ComprobanteRepository comprobanteRepository = mock(ComprobanteRepository.class);
        ComprobanteDetalleRepository detalleRepository = mock(ComprobanteDetalleRepository.class);
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        ComprobanteCorreoService correoService = mock(ComprobanteCorreoService.class);

        ComprobanteEntity comprobante = comprobanteBase();
        PedidoEntity pedido = new PedidoEntity();
        pedido.setIdPedido(11L);
        pedido.setIdCliente(7L);
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(7L);
        usuario.setEmail("cliente@test.com");

        when(comprobanteRepository.findById(31L)).thenReturn(Optional.of(comprobante));
        when(pedidoRepository.findById(11L)).thenReturn(Optional.of(pedido));
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));
        when(comprobanteRepository.save(any(ComprobanteEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(detalleRepository.findByIdComprobanteOrderByIdComprobanteDetalleAsc(31L)).thenReturn(List.of());

        ComprobanteService service = new ComprobanteService(
            comprobanteRepository,
            detalleRepository,
            mock(SerieComprobanteRepository.class),
            mock(EmpresaRepository.class),
            pedidoRepository,
            mock(PedidoItemRepository.class),
            usuarioRepository,
            correoService,
            mock(AuditoriaService.class)
        );

        ComprobanteResponse response = service.enviarCorreoAdmin(31L, 99L);

        assertThat(response.correoEnviado()).isTrue();
        assertThat(response.correoDestino()).isEqualTo("cliente@test.com");
        assertThat(response.fechaCorreoEnvio()).isNotNull();
        assertThat(response.correoError()).isNull();
        verify(correoService).enviar(comprobante, "cliente@test.com");
    }

    @Test
    void enviarCorreoAdminNoDuplicaEnvioSiYaFueEnviado() {
        ComprobanteRepository comprobanteRepository = mock(ComprobanteRepository.class);
        ComprobanteDetalleRepository detalleRepository = mock(ComprobanteDetalleRepository.class);
        ComprobanteCorreoService correoService = mock(ComprobanteCorreoService.class);

        ComprobanteEntity comprobante = comprobanteBase();
        comprobante.setCorreoEnviado(true);
        comprobante.setCorreoDestino("cliente@test.com");
        comprobante.setFechaCorreoEnvio(LocalDateTime.now());

        when(comprobanteRepository.findById(31L)).thenReturn(Optional.of(comprobante));
        when(detalleRepository.findByIdComprobanteOrderByIdComprobanteDetalleAsc(31L)).thenReturn(List.of());

        ComprobanteService service = new ComprobanteService(
            comprobanteRepository,
            detalleRepository,
            mock(SerieComprobanteRepository.class),
            mock(EmpresaRepository.class),
            mock(PedidoRepository.class),
            mock(PedidoItemRepository.class),
            mock(UsuarioRepository.class),
            correoService,
            mock(AuditoriaService.class)
        );

        ComprobanteResponse response = service.enviarCorreoAdmin(31L, 99L);

        assertThat(response.correoEnviado()).isTrue();
        verifyNoInteractions(correoService);
    }

    @Test
    void enviarCorreoAdminGuardaErrorSiFallaElEnvio() {
        ComprobanteRepository comprobanteRepository = mock(ComprobanteRepository.class);
        ComprobanteDetalleRepository detalleRepository = mock(ComprobanteDetalleRepository.class);
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        ComprobanteCorreoService correoService = mock(ComprobanteCorreoService.class);

        ComprobanteEntity comprobante = comprobanteBase();
        PedidoEntity pedido = new PedidoEntity();
        pedido.setIdPedido(11L);
        pedido.setIdCliente(7L);
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(7L);
        usuario.setEmail("cliente@test.com");

        when(comprobanteRepository.findById(31L)).thenReturn(Optional.of(comprobante));
        when(pedidoRepository.findById(11L)).thenReturn(Optional.of(pedido));
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));
        when(comprobanteRepository.save(any(ComprobanteEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(detalleRepository.findByIdComprobanteOrderByIdComprobanteDetalleAsc(31L)).thenReturn(List.of());
        org.mockito.Mockito.doThrow(new IllegalStateException("smtp no disponible"))
            .when(correoService).enviar(comprobante, "cliente@test.com");

        ComprobanteService service = new ComprobanteService(
            comprobanteRepository,
            detalleRepository,
            mock(SerieComprobanteRepository.class),
            mock(EmpresaRepository.class),
            pedidoRepository,
            mock(PedidoItemRepository.class),
            usuarioRepository,
            correoService,
            mock(AuditoriaService.class)
        );

        ComprobanteResponse response = service.enviarCorreoAdmin(31L, 99L);

        assertThat(response.correoEnviado()).isFalse();
        assertThat(response.correoDestino()).isEqualTo("cliente@test.com");
        assertThat(response.fechaCorreoEnvio()).isNull();
        assertThat(response.correoError()).contains("smtp no disponible");
    }

    private ComprobanteEntity comprobanteBase() {
        ComprobanteEntity comprobante = new ComprobanteEntity();
        comprobante.setIdComprobante(31L);
        comprobante.setIdEmpresa(1L);
        comprobante.setIdPedido(11L);
        comprobante.setTipo(TipoComprobante.BOLETA);
        comprobante.setSerie("B001");
        comprobante.setCorrelativo(1L);
        comprobante.setNumeroCompleto("B001-000000001");
        comprobante.setEstado(EstadoComprobante.EMITIDO);
        comprobante.setSubtotal(new BigDecimal("10.00"));
        comprobante.setImpuestoTotal(new BigDecimal("1.80"));
        comprobante.setTotal(new BigDecimal("11.80"));
        comprobante.setFechaEmision(LocalDateTime.now());
        comprobante.setCorreoEnviado(false);
        return comprobante;
    }
}
