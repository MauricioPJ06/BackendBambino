package com.bambino.catalogo.service;

import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.catalogo.dto.OfertaRequest;
import com.bambino.catalogo.entity.OfertaEntity;
import com.bambino.catalogo.entity.ProductoEntity;
import com.bambino.catalogo.mapper.CatalogoMapper;
import com.bambino.catalogo.repository.CategoriaProductoRepository;
import com.bambino.catalogo.repository.OfertaProductoRepository;
import com.bambino.catalogo.repository.OfertaRepository;
import com.bambino.catalogo.repository.ProductoRepository;
import com.bambino.shared.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogoAdminServiceOfertaTest {

    @Mock private CategoriaProductoRepository categoriaRepository;
    @Mock private ProductoRepository productoRepository;
    @Mock private OfertaRepository ofertaRepository;
    @Mock private OfertaProductoRepository ofertaProductoRepository;
    @Mock private AuditoriaService auditoriaService;

    private CatalogoAdminService service;

    @BeforeEach
    void setUp() {
        service = new CatalogoAdminService(
            categoriaRepository,
            productoRepository,
            ofertaRepository,
            ofertaProductoRepository,
            new CatalogoMapper(),
            auditoriaService
        );
    }

    @Test
    void crearOferta_rechazaPorcentajeMayorA100() {
        OfertaRequest request = requestBase(
            "PORCENTAJE",
            new BigDecimal("120.00"),
            null,
            List.of(10L)
        );

        NegocioException ex = assertThrows(NegocioException.class, () -> service.crearOferta(request));

        assertEquals("porcentaje de oferta debe estar entre 0.01 y 100", ex.getMessage());
        verify(ofertaRepository, never()).save(any());
    }

    @Test
    void crearOferta_rechazaOfertaSinProductos() {
        OfertaRequest request = requestBase(
            "MONTO_FIJO",
            new BigDecimal("5.00"),
            null,
            List.of()
        );

        NegocioException ex = assertThrows(NegocioException.class, () -> service.crearOferta(request));

        assertEquals("oferta requiere al menos un producto", ex.getMessage());
        verify(ofertaRepository, never()).save(any());
    }

    @Test
    void crearOferta_rechazaActivaVencida() {
        OfertaRequest request = new OfertaRequest(
            "Promo vencida",
            "PORCENTAJE",
            new BigDecimal("20.00"),
            null,
            "ACTIVA",
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now().minusDays(1),
            List.of(10L)
        );

        NegocioException ex = assertThrows(NegocioException.class, () -> service.crearOferta(request));

        assertEquals("fechaFin debe ser futura para una oferta activa", ex.getMessage());
        verify(ofertaRepository, never()).save(any());
    }

    @Test
    void crearOferta_rechazaActivaFutura() {
        OfertaRequest request = new OfertaRequest(
            "Promo futura",
            "PORCENTAJE",
            new BigDecimal("20.00"),
            null,
            "ACTIVA",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            List.of(10L)
        );

        NegocioException ex = assertThrows(NegocioException.class, () -> service.crearOferta(request));

        assertEquals("fechaInicio debe ser ahora o anterior para una oferta activa", ex.getMessage());
        verify(ofertaRepository, never()).save(any());
    }

    @Test
    void crearOferta_precioEspecialGuardaProductosSinModificarPrecioBase() {
        ProductoEntity producto = new ProductoEntity();
        producto.setIdProducto(10L);
        producto.setPrecioBase(new BigDecimal("25.00"));

        OfertaEntity guardada = new OfertaEntity();
        guardada.setIdOferta(99L);
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(ofertaRepository.save(any(OfertaEntity.class))).thenAnswer(invocation -> {
            OfertaEntity oferta = invocation.getArgument(0);
            oferta.setIdOferta(99L);
            return oferta;
        });
        when(ofertaProductoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.crearOferta(requestBase(
            "PRECIO_ESPECIAL",
            null,
            new BigDecimal("19.90"),
            List.of(10L)
        ));

        assertEquals(99L, response.idOferta());
        assertEquals(List.of(10L), response.idsProductos());
        assertEquals(new BigDecimal("25.00"), producto.getPrecioBase());
    }

    private OfertaRequest requestBase(String tipo, BigDecimal valorDescuento, BigDecimal precioEspecial, List<Long> idsProductos) {
        return new OfertaRequest(
            "Promo test",
            tipo,
            valorDescuento,
            precioEspecial,
            "ACTIVA",
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now().plusDays(1),
            idsProductos
        );
    }
}
