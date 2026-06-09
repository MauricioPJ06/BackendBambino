package com.bambino.comprobantes.service;

import com.bambino.comprobantes.dto.ComprobantePdfResponse;
import com.bambino.comprobantes.entity.ComprobanteDetalleEntity;
import com.bambino.comprobantes.entity.ComprobanteEntity;
import com.bambino.comprobantes.entity.EmpresaEntity;
import com.bambino.comprobantes.entity.EstadoComprobante;
import com.bambino.comprobantes.entity.TipoComprobante;
import com.bambino.comprobantes.entity.TipoDocumentoReceptor;
import com.bambino.comprobantes.repository.ComprobanteDetalleRepository;
import com.bambino.comprobantes.repository.ComprobanteRepository;
import com.bambino.comprobantes.repository.EmpresaRepository;
import com.bambino.pagos.repository.PagoRepository;
import com.bambino.pedidos.entity.PedidoEntity;
import com.bambino.pedidos.repository.PedidoRepository;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ComprobantePdfServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void generarPdfAdminGuardaArchivoConTokenYEndpointPublicoLoReutiliza() throws Exception {
        ComprobanteRepository comprobanteRepository = mock(ComprobanteRepository.class);
        ComprobanteDetalleRepository detalleRepository = mock(ComprobanteDetalleRepository.class);
        EmpresaRepository empresaRepository = mock(EmpresaRepository.class);
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        PagoRepository pagoRepository = mock(PagoRepository.class);
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);

        ComprobanteEntity comprobante = comprobanteBase();
        PedidoEntity pedido = new PedidoEntity();
        pedido.setIdPedido(11L);
        pedido.setIdCliente(7L);
        EmpresaEntity empresa = empresaBase();
        ComprobanteDetalleEntity detalle = detalleBase();
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(7L);
        usuario.setEmail("cliente@test.com");
        usuario.setNombres("Cliente");
        usuario.setApellidos("Prueba");

        when(comprobanteRepository.findById(31L)).thenReturn(Optional.of(comprobante));
        when(comprobanteRepository.save(any(ComprobanteEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(pedidoRepository.findById(11L)).thenReturn(Optional.of(pedido));
        when(detalleRepository.findByIdComprobanteOrderByIdComprobanteDetalleAsc(31L)).thenReturn(List.of(detalle));
        when(pagoRepository.findByIdPedidoOrderByFechaCreacionDesc(11L)).thenReturn(List.of());
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));

        ComprobantePdfService service = new ComprobantePdfService(
            comprobanteRepository,
            detalleRepository,
            empresaRepository,
            pedidoRepository,
            pagoRepository,
            usuarioRepository,
            tempDir.toString(),
            "https://bambino.test/"
        );

        ComprobantePdfResponse adminPdf = service.generarPdfAdmin(31L);

        assertThat(adminPdf.filename()).isEqualTo("B001-000000001.pdf");
        assertThat(adminPdf.contenido()).isNotEmpty();
        assertThat(comprobante.getPdfToken()).isNotBlank();
        assertThat(comprobante.getPdfPath()).startsWith("comprobantes/");
        assertThat(comprobante.getFechaPdfGenerado()).isNotNull();
        assertThat(Files.exists(tempDir.resolve(comprobante.getPdfPath()))).isTrue();

        when(comprobanteRepository.findByPdfToken(comprobante.getPdfToken())).thenReturn(Optional.of(comprobante));

        ComprobantePdfResponse publicPdf = service.generarPdfPublicoPorToken(comprobante.getPdfToken());

        assertThat(publicPdf.filename()).isEqualTo(adminPdf.filename());
        assertThat(publicPdf.contenido()).isEqualTo(adminPdf.contenido());
    }

    @Test
    void generarPdfClientePorPedidoValidaPropietarioYGeneraPdf() throws Exception {
        ComprobanteRepository comprobanteRepository = mock(ComprobanteRepository.class);
        ComprobanteDetalleRepository detalleRepository = mock(ComprobanteDetalleRepository.class);
        EmpresaRepository empresaRepository = mock(EmpresaRepository.class);
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        PagoRepository pagoRepository = mock(PagoRepository.class);
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(7L);
        usuario.setEmail("cliente@test.com");
        usuario.setNombres("Cliente");
        usuario.setApellidos("Prueba");

        PedidoEntity pedido = new PedidoEntity();
        pedido.setIdPedido(11L);
        pedido.setIdCliente(7L);

        ComprobanteEntity comprobante = comprobanteBase();

        when(usuarioRepository.findByEmail("cliente@test.com")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));
        when(pedidoRepository.findById(11L)).thenReturn(Optional.of(pedido));
        when(comprobanteRepository.findByIdPedido(11L)).thenReturn(Optional.of(comprobante));
        when(comprobanteRepository.save(any(ComprobanteEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresaBase()));
        when(detalleRepository.findByIdComprobanteOrderByIdComprobanteDetalleAsc(31L)).thenReturn(List.of(detalleBase()));
        when(pagoRepository.findByIdPedidoOrderByFechaCreacionDesc(11L)).thenReturn(List.of());

        ComprobantePdfService service = new ComprobantePdfService(
            comprobanteRepository,
            detalleRepository,
            empresaRepository,
            pedidoRepository,
            pagoRepository,
            usuarioRepository,
            tempDir.toString(),
            "https://bambino.test/"
        );

        ComprobantePdfResponse response = service.generarPdfClientePorPedido("cliente@test.com", 11L);

        assertThat(response.filename()).isEqualTo("B001-000000001.pdf");
        assertThat(response.contenido()).isNotEmpty();
        assertThat(comprobante.getPdfPath()).startsWith("comprobantes/");
    }

    @Test
    void generarPdfClientePorPedidoRechazaPedidoDeOtroCliente() {
        ComprobanteRepository comprobanteRepository = mock(ComprobanteRepository.class);
        ComprobanteDetalleRepository detalleRepository = mock(ComprobanteDetalleRepository.class);
        EmpresaRepository empresaRepository = mock(EmpresaRepository.class);
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        PagoRepository pagoRepository = mock(PagoRepository.class);
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(7L);
        usuario.setEmail("cliente@test.com");

        PedidoEntity pedido = new PedidoEntity();
        pedido.setIdPedido(11L);
        pedido.setIdCliente(99L);

        when(usuarioRepository.findByEmail("cliente@test.com")).thenReturn(Optional.of(usuario));
        when(pedidoRepository.findById(11L)).thenReturn(Optional.of(pedido));

        ComprobantePdfService service = new ComprobantePdfService(
            comprobanteRepository,
            detalleRepository,
            empresaRepository,
            pedidoRepository,
            pagoRepository,
            usuarioRepository,
            tempDir.toString(),
            "https://bambino.test/"
        );

        assertThatThrownBy(() -> service.generarPdfClientePorPedido("cliente@test.com", 11L))
            .hasMessageContaining("el pedido no pertenece al cliente autenticado");
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
        comprobante.setDocReceptorTipo(TipoDocumentoReceptor.DNI);
        comprobante.setDocReceptorNumero("12345678");
        comprobante.setSubtotal(new BigDecimal("10.00"));
        comprobante.setImpuestoTotal(new BigDecimal("1.80"));
        comprobante.setTotal(new BigDecimal("11.80"));
        comprobante.setFechaEmision(LocalDateTime.of(2026, 6, 4, 10, 15));
        comprobante.setCorreoEnviado(false);
        return comprobante;
    }

    private EmpresaEntity empresaBase() {
        EmpresaEntity empresa = new EmpresaEntity();
        empresa.setIdEmpresa(1L);
        empresa.setRuc("20600000001");
        empresa.setRazonSocial("Bambino Chicken SAC");
        empresa.setNombreComercial("Bambino Chicken");
        empresa.setDireccionFiscal("Av. Prueba 123");
        empresa.setActivo(true);
        return empresa;
    }

    private ComprobanteDetalleEntity detalleBase() {
        ComprobanteDetalleEntity detalle = new ComprobanteDetalleEntity();
        detalle.setIdComprobanteDetalle(41L);
        detalle.setIdComprobante(31L);
        detalle.setDescripcionItem("1/4 Pollo a la brasa");
        detalle.setCantidad(BigDecimal.ONE);
        detalle.setPrecioUnitario(new BigDecimal("10.00"));
        detalle.setDescuentoUnitario(BigDecimal.ZERO);
        detalle.setSubtotalLinea(new BigDecimal("10.00"));
        detalle.setFechaCreacion(LocalDateTime.of(2026, 6, 4, 10, 15));
        return detalle;
    }
}
