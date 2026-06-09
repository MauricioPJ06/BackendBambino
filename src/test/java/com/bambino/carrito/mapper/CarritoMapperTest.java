package com.bambino.carrito.mapper;

import com.bambino.carrito.dto.CarritoItemResponse;
import com.bambino.carrito.entity.CarritoItemEntity;
import com.bambino.catalogo.entity.ProductoEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CarritoMapperTest {

    private final CarritoMapper mapper = new CarritoMapper();

    @Test
    void mapsProductImageUrlIntoCartItemResponse() {
        CarritoItemEntity item = new CarritoItemEntity();
        item.setIdCarritoItem(1L);
        item.setIdProducto(10L);
        item.setCantidad(BigDecimal.ONE);
        item.setPrecioUnitarioSnapshot(new BigDecimal("16.00"));
        item.setDescuentoUnitarioSnapshot(BigDecimal.ZERO);

        ProductoEntity producto = new ProductoEntity();
        producto.setNombre("1/4 de Pollo");
        producto.setImagenUrl("https://cdn.example.com/pollo.webp");

        CarritoItemResponse response = mapper.toItemResponse(item, producto);

        assertThat(response.imagenUrl()).isEqualTo("https://cdn.example.com/pollo.webp");
    }
}
