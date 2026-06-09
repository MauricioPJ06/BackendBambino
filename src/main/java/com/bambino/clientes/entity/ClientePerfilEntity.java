package com.bambino.clientes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "cliente_perfil")
/**
 * Clase que maneja la funcionalidad de ClientePerfilEntity.
 */
public class ClientePerfilEntity {

    @Id
    @Column(name = "id_cliente")
    private Long idCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_tipo", nullable = false, length = 10)
    private DocTipo docTipo;

    @Column(name = "doc_numero", length = 20)
    private String docNumero;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public DocTipo getDocTipo() { return docTipo; }
    public void setDocTipo(DocTipo docTipo) { this.docTipo = docTipo; }
    public String getDocNumero() { return docNumero; }
    public void setDocNumero(String docNumero) { this.docNumero = docNumero; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
