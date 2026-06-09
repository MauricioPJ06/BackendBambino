package com.bambino.catalogo.repository;

import com.bambino.catalogo.entity.EstadoProducto;
import com.bambino.catalogo.entity.ProductoEntity;
import com.bambino.pedidos.entity.EstadoPedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de ProductoRepository.
 */
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    @EntityGraph(attributePaths = "categoria")
    List<ProductoEntity> findByEstadoAndVisibleWebTrueAndDisponibleTrueOrderByOrdenVisualAscNombreAsc(EstadoProducto estado);

    @EntityGraph(attributePaths = "categoria")
    Optional<ProductoEntity> findByIdProductoAndEstadoAndVisibleWebTrueAndDisponibleTrue(Long idProducto, EstadoProducto estado);

    @EntityGraph(attributePaths = "categoria")
    List<ProductoEntity> findByEstadoAndVisibleWebTrueAndDisponibleTrueAndCategoria_NombreIgnoreCaseOrderByOrdenVisualAscNombreAsc(EstadoProducto estado, String nombreCategoria);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdProductoNot(String nombre, Long idProducto);
    boolean existsByOrdenVisual(Integer ordenVisual);
    boolean existsByOrdenVisualAndIdProductoNot(Integer ordenVisual, Long idProducto);

    @EntityGraph(attributePaths = "categoria")
    @Query("""
        select p
        from ProductoEntity p
        left join p.categoria c
        where p.estado = :estado
          and p.visibleWeb = true
          and p.disponible = true
          and (:idCategoria is null or c.idCategoria = :idCategoria)
          and (:q is null or :q = '' or lower(p.nombre) like concat('%', :q, '%') or lower(p.descripcion) like concat('%', :q, '%'))
        order by p.ordenVisual asc, p.nombre asc
        """)
    List<ProductoEntity> findPublicosFiltrados(
        @Param("estado") EstadoProducto estado,
        @Param("idCategoria") Long idCategoria,
        @Param("q") String q
    );

    @EntityGraph(attributePaths = "categoria")
    List<ProductoEntity> findByIdProductoIn(List<Long> ids);

    @Query("""
        select p.idProducto
        from ProductoEntity p, com.bambino.pedidos.entity.PedidoItemEntity pi, com.bambino.pedidos.entity.PedidoEntity pe
        where pi.idProducto = p.idProducto
          and pe.idPedido = pi.idPedido
          and pe.fechaCreacion >= :desde
          and pe.fechaCreacion < :hasta
          and pe.estadoActual in :estados
          and p.estado = com.bambino.catalogo.entity.EstadoProducto.ACTIVO
          and p.visibleWeb = true
          and p.disponible = true
        group by p.idProducto
        order by sum(pi.cantidad) desc, sum(pi.subtotalLinea) desc, max(pe.fechaCreacion) desc
        """)
    List<Long> findTopProductoIdsVendidosEntreFechas(
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta,
        @Param("estados") List<EstadoPedido> estados
    );
}
