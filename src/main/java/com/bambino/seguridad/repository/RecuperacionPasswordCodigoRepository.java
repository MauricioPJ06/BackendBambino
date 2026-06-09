package com.bambino.seguridad.repository;

import com.bambino.seguridad.entity.EstadoRecuperacionCodigo;
import com.bambino.seguridad.entity.RecuperacionPasswordCodigoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de RecuperacionPasswordCodigoRepository.
 */
public interface RecuperacionPasswordCodigoRepository extends JpaRepository<RecuperacionPasswordCodigoEntity, Long> {

    Optional<RecuperacionPasswordCodigoEntity> findFirstByCorreoAndEstadoOrderByIdCodigoDesc(String correo, EstadoRecuperacionCodigo estado);

    List<RecuperacionPasswordCodigoEntity> findByCorreoAndEstado(String correo, EstadoRecuperacionCodigo estado);

    List<RecuperacionPasswordCodigoEntity> findTop5ByCorreoOrderByIdCodigoDesc(String correo);
}
