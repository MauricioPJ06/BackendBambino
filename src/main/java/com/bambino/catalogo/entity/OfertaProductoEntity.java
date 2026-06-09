package com.bambino.catalogo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "oferta_producto")
/**
 * Clase que maneja la funcionalidad de OfertaProductoEntity.
 */
public class OfertaProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferta_producto")
    private Long idOfertaProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oferta", nullable = false)
    private OfertaEntity oferta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    public Long getIdOfertaProducto() { return idOfertaProducto; }
    public void setIdOfertaProducto(Long idOfertaProducto) { this.idOfertaProducto = idOfertaProducto; }
    public OfertaEntity getOferta() { return oferta; }
    public void setOferta(OfertaEntity oferta) { this.oferta = oferta; }
    public ProductoEntity getProducto() { return producto; }
    public void setProducto(ProductoEntity producto) { this.producto = producto; }
}
