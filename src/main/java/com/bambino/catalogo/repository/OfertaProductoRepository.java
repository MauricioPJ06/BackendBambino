package com.bambino.catalogo.repository;

import com.bambino.catalogo.entity.OfertaProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de OfertaProductoRepository.
 */
public interface OfertaProductoRepository extends JpaRepository<OfertaProductoEntity, Long> {
    List<OfertaProductoEntity> findByOfertaIdOferta(Long idOferta);
    void deleteByOfertaIdOferta(Long idOferta);

    @Query("""
        select op
        from OfertaProductoEntity op
        join fetch op.producto
        join op.oferta o
        where op.producto.idProducto = :idProducto
          and o.estado = com.bambino.catalogo.entity.EstadoOferta.ACTIVA
          and (o.fechaInicio is null or o.fechaInicio <= :ahora)
          and (o.fechaFin is null or o.fechaFin >= :ahora)
        order by o.fechaInicio desc, o.idOferta desc
        """)
    List<OfertaProductoEntity> findOfertasActivasByProducto(@Param("idProducto") Long idProducto,
                                                             @Param("ahora") LocalDateTime ahora);

    @Query("""
        select op
        from OfertaProductoEntity op
        join fetch op.producto
        where op.oferta.idOferta in :ofertaIds
        """)
    List<OfertaProductoEntity> findByOfertaIdOfertaInWithProducto(@Param("ofertaIds") List<Long> ofertaIds);
}
