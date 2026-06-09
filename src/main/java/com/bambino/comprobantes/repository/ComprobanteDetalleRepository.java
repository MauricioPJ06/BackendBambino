package com.bambino.comprobantes.repository;

import com.bambino.comprobantes.entity.ComprobanteDetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de ComprobanteDetalleRepository.
 */
public interface ComprobanteDetalleRepository extends JpaRepository<ComprobanteDetalleEntity, Long> {
    List<ComprobanteDetalleEntity> findByIdComprobanteOrderByIdComprobanteDetalleAsc(Long idComprobante);
}
