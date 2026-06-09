package com.bambino.carrito.repository;

import com.bambino.carrito.entity.CarritoItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de CarritoItemRepository.
 */
public interface CarritoItemRepository extends JpaRepository<CarritoItemEntity, Long> {
    List<CarritoItemEntity> findByIdCarritoOrderByIdCarritoItemAsc(Long idCarrito);
    Optional<CarritoItemEntity> findByIdCarritoAndIdProducto(Long idCarrito, Long idProducto);
    Optional<CarritoItemEntity> findByIdCarritoItemAndIdCarrito(Long idCarritoItem, Long idCarrito);
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from CarritoItemEntity i where i.idCarritoItem = :idCarritoItem and i.idCarrito = :idCarrito")
    int deleteByIdCarritoItemAndIdCarrito(@Param("idCarritoItem") Long idCarritoItem, @Param("idCarrito") Long idCarrito);
    void deleteByIdCarrito(Long idCarrito);
}
