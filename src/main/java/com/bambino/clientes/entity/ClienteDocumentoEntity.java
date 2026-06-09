package com.bambino.clientes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "cliente_documento")
/**
 * Clase que maneja la funcionalidad de ClienteDocumentoEntity.
 */
public class ClienteDocumentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_tipo", nullable = false, length = 10)
    private DocTipo docTipo;

    @Column(name = "doc_numero", nullable = false, length = 20)
    private String docNumero;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public DocTipo getDocTipo() { return docTipo; }
    public void setDocTipo(DocTipo docTipo) { this.docTipo = docTipo; }
    public String getDocNumero() { return docNumero; }
    public void setDocNumero(String docNumero) { this.docNumero = docNumero; }
    public Boolean getEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(Boolean esPrincipal) { this.esPrincipal = esPrincipal; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
