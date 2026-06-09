package com.bambino.clientes.repository;

import com.bambino.clientes.entity.ClienteDocumentoEntity;
import com.bambino.clientes.entity.DocTipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de ClienteDocumentoRepository.
 */
public interface ClienteDocumentoRepository extends JpaRepository<ClienteDocumentoEntity, Long> {
    List<ClienteDocumentoEntity> findByIdClienteAndActivoTrueOrderByEsPrincipalDescFechaActualizacionDesc(Long idCliente);
    Optional<ClienteDocumentoEntity> findByIdDocumentoAndIdClienteAndActivoTrue(Long idDocumento, Long idCliente);
    Optional<ClienteDocumentoEntity> findFirstByIdClienteAndEsPrincipalTrueAndActivoTrue(Long idCliente);
    Optional<ClienteDocumentoEntity> findByIdClienteAndDocTipoAndDocNumeroAndActivoTrue(Long idCliente, DocTipo docTipo, String docNumero);
    boolean existsByIdClienteAndDocTipoAndDocNumeroAndActivoTrue(Long idCliente, DocTipo docTipo, String docNumero);
    boolean existsByIdClienteAndDocTipoAndActivoTrue(Long idCliente, DocTipo docTipo);
}
