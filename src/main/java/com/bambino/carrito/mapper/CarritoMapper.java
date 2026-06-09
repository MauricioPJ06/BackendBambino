package com.bambino.carrito.mapper;

import com.bambino.carrito.dto.CarritoItemResponse;
import com.bambino.carrito.entity.CarritoItemEntity;
import com.bambino.catalogo.entity.ProductoEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
/**
 * Clase que maneja la funcionalidad de CarritoMapper.
 */
public class CarritoMapper {

    /**
     * Metodo que realiza la operacion de toItemResponse.
     * @param item parametro de entrada para la operacion
     * @param producto parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CarritoItemResponse toItemResponse(CarritoItemEntity item, ProductoEntity producto) {
        BigDecimal subtotal = item.getPrecioUnitarioSnapshot()
            .subtract(item.getDescuentoUnitarioSnapshot())
            .multiply(item.getCantidad());

        return new CarritoItemResponse(
            item.getIdCarritoItem(),
            item.getIdProducto(),
            producto == null ? "producto no disponible" : producto.getNombre(),
            item.getCantidad(),
            item.getPrecioUnitarioSnapshot(),
            item.getDescuentoUnitarioSnapshot(),
            subtotal,
            item.getObservacion(),
            producto == null ? null : producto.getImagenUrl()
        );
    }
}
