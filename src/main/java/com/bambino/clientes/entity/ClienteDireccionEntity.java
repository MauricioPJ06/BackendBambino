package com.bambino.clientes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente_direccion")
/**
 * Clase que maneja la funcionalidad de ClienteDireccionEntity.
 */
public class ClienteDireccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(name = "direccion_linea1", nullable = false, length = 220)
    private String direccionLinea1;

    @Column(name = "referencia", length = 220)
    private String referencia;

    @Column(name = "distrito", length = 120)
    private String distrito;

    @Column(name = "ciudad", length = 120)
    private String ciudad;

    @Column(name = "latitud", precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 10, scale = 7)
    private BigDecimal longitud;

    @Column(name = "google_place_id", length = 120)
    private String googlePlaceId;

    @Column(name = "google_plus_code", length = 40)
    private String googlePlusCode;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdDireccion() { return idDireccion; }
    public void setIdDireccion(Long idDireccion) { this.idDireccion = idDireccion; }
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public String getDireccionLinea1() { return direccionLinea1; }
    public void setDireccionLinea1(String direccionLinea1) { this.direccionLinea1 = direccionLinea1; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }
    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }
    public String getGooglePlaceId() { return googlePlaceId; }
    public void setGooglePlaceId(String googlePlaceId) { this.googlePlaceId = googlePlaceId; }
    public String getGooglePlusCode() { return googlePlusCode; }
    public void setGooglePlusCode(String googlePlusCode) { this.googlePlusCode = googlePlusCode; }
    public Boolean getEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(Boolean esPrincipal) { this.esPrincipal = esPrincipal; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
