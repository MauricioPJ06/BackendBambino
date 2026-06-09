package com.bambino.carrito.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "zona_delivery")
/**
 * Clase que maneja la funcionalidad de ZonaDeliveryEntity.
 */
public class ZonaDeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Long idZona;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "tarifa_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaBase;

    @Column(name = "monto_minimo", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoMinimo;

    @Column(name = "tiempo_estimado_minutos", nullable = false)
    private Integer tiempoEstimadoMinutos;

    @Column(name = "cobertura_descripcion", length = 300)
    private String coberturaDescripcion;

    @Column(name = "latitud_centro", precision = 10, scale = 7)
    private BigDecimal latitudCentro;

    @Column(name = "longitud_centro", precision = 10, scale = 7)
    private BigDecimal longitudCentro;

    @Column(name = "radio_km", precision = 10, scale = 2)
    private BigDecimal radioKm;

    @Column(name = "mapa_embed_url")
    private String mapaEmbedUrl;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "hora_inicio_atencion")
    private LocalTime horaInicioAtencion;

    @Column(name = "hora_fin_atencion")
    private LocalTime horaFinAtencion;

    public Long getIdZona() { return idZona; }
    public void setIdZona(Long idZona) { this.idZona = idZona; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public BigDecimal getTarifaBase() { return tarifaBase; }
    public void setTarifaBase(BigDecimal tarifaBase) { this.tarifaBase = tarifaBase; }
    public BigDecimal getMontoMinimo() { return montoMinimo; }
    public void setMontoMinimo(BigDecimal montoMinimo) { this.montoMinimo = montoMinimo; }
    public Integer getTiempoEstimadoMinutos() { return tiempoEstimadoMinutos; }
    public void setTiempoEstimadoMinutos(Integer tiempoEstimadoMinutos) { this.tiempoEstimadoMinutos = tiempoEstimadoMinutos; }
    public String getCoberturaDescripcion() { return coberturaDescripcion; }
    public void setCoberturaDescripcion(String coberturaDescripcion) { this.coberturaDescripcion = coberturaDescripcion; }
    public BigDecimal getLatitudCentro() { return latitudCentro; }
    public void setLatitudCentro(BigDecimal latitudCentro) { this.latitudCentro = latitudCentro; }
    public BigDecimal getLongitudCentro() { return longitudCentro; }
    public void setLongitudCentro(BigDecimal longitudCentro) { this.longitudCentro = longitudCentro; }
    public BigDecimal getRadioKm() { return radioKm; }
    public void setRadioKm(BigDecimal radioKm) { this.radioKm = radioKm; }
    public String getMapaEmbedUrl() { return mapaEmbedUrl; }
    public void setMapaEmbedUrl(String mapaEmbedUrl) { this.mapaEmbedUrl = mapaEmbedUrl; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public LocalTime getHoraInicioAtencion() { return horaInicioAtencion; }
    public void setHoraInicioAtencion(LocalTime horaInicioAtencion) { this.horaInicioAtencion = horaInicioAtencion; }
    public LocalTime getHoraFinAtencion() { return horaFinAtencion; }
    public void setHoraFinAtencion(LocalTime horaFinAtencion) { this.horaFinAtencion = horaFinAtencion; }
}
