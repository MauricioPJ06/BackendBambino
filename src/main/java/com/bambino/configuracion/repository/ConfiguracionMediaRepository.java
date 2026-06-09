package com.bambino.configuracion.repository;

import com.bambino.configuracion.entity.ConfiguracionMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de ConfiguracionMediaRepository.
 */
public interface ConfiguracionMediaRepository extends JpaRepository<ConfiguracionMediaEntity, Long> {
    Optional<ConfiguracionMediaEntity> findByClave(String clave);
    List<ConfiguracionMediaEntity> findByActivaTrueOrderByClaveAsc();
}
