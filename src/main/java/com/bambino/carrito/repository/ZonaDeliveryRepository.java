package com.bambino.carrito.repository;

import com.bambino.carrito.entity.ZonaDeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de ZonaDeliveryRepository.
 */
public interface ZonaDeliveryRepository extends JpaRepository<ZonaDeliveryEntity, Long> {
    List<ZonaDeliveryEntity> findByActivoTrueOrderByTarifaBaseAscIdZonaAsc();
}
