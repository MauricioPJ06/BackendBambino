package com.bambino.comprobantes.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "empresa")
/**
 * Clase que maneja la funcionalidad de EmpresaEntity.
 */
public class EmpresaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long idEmpresa;

    @Column(name = "ruc", nullable = false, length = 20)
    private String ruc;

    @Column(name = "razon_social", nullable = false, length = 220)
    private String razonSocial;

    @Column(name = "nombre_comercial", length = 220)
    private String nombreComercial;

    @Column(name = "direccion_fiscal", nullable = false, length = 300)
    private String direccionFiscal;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "correo", length = 190)
    private String correo;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }
    public String getDireccionFiscal() { return direccionFiscal; }
    public void setDireccionFiscal(String direccionFiscal) { this.direccionFiscal = direccionFiscal; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
