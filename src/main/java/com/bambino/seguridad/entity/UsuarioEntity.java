package com.bambino.seguridad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
/**
 * Clase que maneja la funcionalidad de UsuarioEntity.
 */
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "uuid_usuario", nullable = false, unique = true, length = 36)
    private String uuidUsuario;

    @Column(name = "email", nullable = false, unique = true, length = 190)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "nombres", nullable = false, length = 120)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 120)
    private String apellidos;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "documento", nullable = false, unique = true, length = 20)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoUsuario estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private RolEntity rol;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    /**
     * Metodo que realiza la operacion de getIdUsuario.
     * @return resultado de la operacion
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Metodo que realiza la operacion de setIdUsuario.
     * @param idUsuario parametro de entrada para la operacion
     */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Metodo que realiza la operacion de getUuidUsuario.
     * @return resultado de la operacion
     */
    public String getUuidUsuario() {
        return uuidUsuario;
    }

    /**
     * Metodo que realiza la operacion de setUuidUsuario.
     * @param uuidUsuario parametro de entrada para la operacion
     */
    public void setUuidUsuario(String uuidUsuario) {
        this.uuidUsuario = uuidUsuario;
    }

    /**
     * Metodo que realiza la operacion de getEmail.
     * @return resultado de la operacion
     */
    public String getEmail() {
        return email;
    }

    /**
     * Metodo que realiza la operacion de setEmail.
     * @param email parametro de entrada para la operacion
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Metodo que realiza la operacion de getPasswordHash.
     * @return resultado de la operacion
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Metodo que realiza la operacion de setPasswordHash.
     * @param passwordHash parametro de entrada para la operacion
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Metodo que realiza la operacion de getNombres.
     * @return resultado de la operacion
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Metodo que realiza la operacion de setNombres.
     * @param nombres parametro de entrada para la operacion
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Metodo que realiza la operacion de getApellidos.
     * @return resultado de la operacion
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Metodo que realiza la operacion de setApellidos.
     * @param apellidos parametro de entrada para la operacion
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Metodo que realiza la operacion de getTelefono.
     * @return resultado de la operacion
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Metodo que realiza la operacion de setTelefono.
     * @param telefono parametro de entrada para la operacion
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Metodo que realiza la operacion de getDocumento.
     * @return resultado de la operacion
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Metodo que realiza la operacion de setDocumento.
     * @param documento parametro de entrada para la operacion
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * Metodo que realiza la operacion de getEstado.
     * @return resultado de la operacion
     */
    public EstadoUsuario getEstado() {
        return estado;
    }

    /**
     * Metodo que realiza la operacion de setEstado.
     * @param estado parametro de entrada para la operacion
     */
    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    /**
     * Metodo que realiza la operacion de getRol.
     * @return resultado de la operacion
     */
    public RolEntity getRol() {
        return rol;
    }

    /**
     * Metodo que realiza la operacion de setRol.
     * @param rol parametro de entrada para la operacion
     */
    public void setRol(RolEntity rol) {
        this.rol = rol;
    }

    /**
     * Metodo que realiza la operacion de getFechaCreacion.
     * @return resultado de la operacion
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Metodo que realiza la operacion de setFechaCreacion.
     * @param fechaCreacion parametro de entrada para la operacion
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Metodo que realiza la operacion de getFechaActualizacion.
     * @return resultado de la operacion
     */
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    /**
     * Metodo que realiza la operacion de setFechaActualizacion.
     * @param fechaActualizacion parametro de entrada para la operacion
     */
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
