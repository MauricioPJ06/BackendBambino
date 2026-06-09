package com.bambino.catalogo.repository;

import com.bambino.catalogo.entity.EstadoOferta;
import com.bambino.catalogo.entity.OfertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de OfertaRepository.
 */
public interface OfertaRepository extends JpaRepository<OfertaEntity, Long> {
    List<OfertaEntity> findByEstadoAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByFechaInicioDesc(
        EstadoOferta estado,
        LocalDateTime ahoraInicio,
        LocalDateTime ahoraFin
    );
}
