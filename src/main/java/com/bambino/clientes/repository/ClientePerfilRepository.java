package com.bambino.clientes.repository;

import com.bambino.clientes.entity.ClientePerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz que maneja la funcionalidad de ClientePerfilRepository.
 */
public interface ClientePerfilRepository extends JpaRepository<ClientePerfilEntity, Long> {
}
