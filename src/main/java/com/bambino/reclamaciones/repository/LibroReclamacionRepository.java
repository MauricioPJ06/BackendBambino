package com.bambino.reclamaciones.repository;

import com.bambino.reclamaciones.entity.LibroReclamacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de LibroReclamacionRepository.
 */
public interface LibroReclamacionRepository extends JpaRepository<LibroReclamacionEntity, Long> {
    Optional<LibroReclamacionEntity> findTopByOrderByIdReclamoDesc();
    List<LibroReclamacionEntity> findByIdClienteOrderByFechaRegistroDesc(Long idCliente);
}
