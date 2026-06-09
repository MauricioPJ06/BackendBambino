package com.bambino.comprobantes.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "serie_comprobante")
/**
 * Clase que maneja la funcionalidad de SerieComprobanteEntity.
 */
public class SerieComprobanteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_serie")
    private Long idSerie;

    @Column(name = "id_empresa", nullable = false)
    private Long idEmpresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante", nullable = false, length = 10)
    private TipoComprobante tipoComprobante;

    @Column(name = "serie", nullable = false, length = 10)
    private String serie;

    @Column(name = "correlativo_actual", nullable = false)
    private Long correlativoActual;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdSerie() { return idSerie; }
    public void setIdSerie(Long idSerie) { this.idSerie = idSerie; }
    public Long getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }
    public TipoComprobante getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(TipoComprobante tipoComprobante) { this.tipoComprobante = tipoComprobante; }
    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }
    public Long getCorrelativoActual() { return correlativoActual; }
    public void setCorrelativoActual(Long correlativoActual) { this.correlativoActual = correlativoActual; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
