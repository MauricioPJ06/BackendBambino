package com.bambino.carrito.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion_global")
/**
 * Clase que maneja la funcionalidad de ConfiguracionGlobalEntity.
 */
public class ConfiguracionGlobalEntity {

    @Id
    @Column(name = "id_config")
    private Short idConfig;

    @Column(name = "igv_porcentaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal igvPorcentaje;

    @Column(name = "delivery_monto_minimo", nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryMontoMinimo;

    @Column(name = "moneda", nullable = false, length = 10)
    private String moneda;

    @Column(name = "delivery_tiempo_min_minutos", nullable = false)
    private Integer deliveryTiempoMinMinutos;

    @Column(name = "delivery_tiempo_max_minutos", nullable = false)
    private Integer deliveryTiempoMaxMinutos;

    @Column(name = "timezone", nullable = false, length = 60)
    private String timezone;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Short getIdConfig() { return idConfig; }
    public void setIdConfig(Short idConfig) { this.idConfig = idConfig; }
    public BigDecimal getIgvPorcentaje() { return igvPorcentaje; }
    public void setIgvPorcentaje(BigDecimal igvPorcentaje) { this.igvPorcentaje = igvPorcentaje; }
    public BigDecimal getDeliveryMontoMinimo() { return deliveryMontoMinimo; }
    public void setDeliveryMontoMinimo(BigDecimal deliveryMontoMinimo) { this.deliveryMontoMinimo = deliveryMontoMinimo; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public Integer getDeliveryTiempoMinMinutos() { return deliveryTiempoMinMinutos; }
    public void setDeliveryTiempoMinMinutos(Integer deliveryTiempoMinMinutos) { this.deliveryTiempoMinMinutos = deliveryTiempoMinMinutos; }
    public Integer getDeliveryTiempoMaxMinutos() { return deliveryTiempoMaxMinutos; }
    public void setDeliveryTiempoMaxMinutos(Integer deliveryTiempoMaxMinutos) { this.deliveryTiempoMaxMinutos = deliveryTiempoMaxMinutos; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
