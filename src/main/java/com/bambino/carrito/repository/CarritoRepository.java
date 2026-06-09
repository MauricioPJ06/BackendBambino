package com.bambino.carrito.repository;

import com.bambino.carrito.entity.CarritoEntity;
import com.bambino.carrito.entity.EstadoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de CarritoRepository.
 */
public interface CarritoRepository extends JpaRepository<CarritoEntity, Long> {
    Optional<CarritoEntity> findFirstByIdClienteAndEstadoOrderByIdCarritoDesc(Long idCliente, EstadoCarrito estado);
    Optional<CarritoEntity> findFirstByIdClienteOrderByIdCarritoDesc(Long idCliente);
}
