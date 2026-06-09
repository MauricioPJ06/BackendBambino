package com.bambino.catalogo.mapper;

import com.bambino.catalogo.dto.*;
import com.bambino.catalogo.entity.CategoriaProductoEntity;
import com.bambino.catalogo.entity.OfertaEntity;
import com.bambino.catalogo.entity.OfertaProductoEntity;
import com.bambino.catalogo.entity.ProductoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
/**
 * Clase que maneja la funcionalidad de CatalogoMapper.
 */
public class CatalogoMapper {

    /**
     * Metodo que realiza la operacion de toCategoriaResponse.
     * @param entity parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CategoriaResponse toCategoriaResponse(CategoriaProductoEntity entity) {
        return new CategoriaResponse(entity.getIdCategoria(), entity.getNombre(), entity.getDescripcion(), entity.getOrdenVisual(), entity.getActiva());
    }

    /**
     * Metodo que realiza la operacion de toProductoResponse.
     * @param entity parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ProductoResponse toProductoResponse(ProductoEntity entity) {
        return new ProductoResponse(
            entity.getIdProducto(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getCategoria() == null ? null : entity.getCategoria().getIdCategoria(),
            entity.getCategoria() == null ? null : entity.getCategoria().getNombre(),
            entity.getPrecioBase(),
            entity.getVisibleWeb(),
            entity.getDisponible(),
            entity.getEstado().name(),
            entity.getImagenUrl(),
            entity.getOrdenVisual(),
            entity.getPrecioBase(),
            java.math.BigDecimal.ZERO.setScale(2),
            null,
            null,
            null
        );
    }

    public ProductoResponse toProductoResponse(ProductoEntity entity,
                                               java.math.BigDecimal precioFinal,
                                               java.math.BigDecimal descuentoAplicado,
                                               OfertaEntity ofertaActiva) {
        return new ProductoResponse(
            entity.getIdProducto(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getCategoria() == null ? null : entity.getCategoria().getIdCategoria(),
            entity.getCategoria() == null ? null : entity.getCategoria().getNombre(),
            entity.getPrecioBase(),
            entity.getVisibleWeb(),
            entity.getDisponible(),
            entity.getEstado().name(),
            entity.getImagenUrl(),
            entity.getOrdenVisual(),
            precioFinal,
            descuentoAplicado,
            ofertaActiva == null ? null : ofertaActiva.getIdOferta(),
            ofertaActiva == null ? null : ofertaActiva.getNombre(),
            ofertaActiva == null ? null : ofertaActiva.getTipo().name()
        );
    }

    /**
     * Metodo que realiza la operacion de toOfertaResponse.
     * @param oferta parametro de entrada para la operacion
     * @param enlaces parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public OfertaResponse toOfertaResponse(OfertaEntity oferta, List<OfertaProductoEntity> enlaces) {
        return new OfertaResponse(
            oferta.getIdOferta(),
            oferta.getNombre(),
            oferta.getTipo().name(),
            oferta.getValorDescuento(),
            oferta.getPrecioEspecial(),
            oferta.getEstado().name(),
            oferta.getFechaInicio(),
            oferta.getFechaFin(),
            enlaces.stream().map(e -> e.getProducto().getIdProducto()).toList()
        );
    }
}
