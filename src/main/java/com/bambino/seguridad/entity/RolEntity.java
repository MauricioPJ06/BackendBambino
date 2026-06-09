package com.bambino.seguridad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rol")
/**
 * Clase que maneja la funcionalidad de RolEntity.
 */
public class RolEntity {

    @Id
    @Column(name = "id_rol")
    private Short idRol;

    @Column(name = "nombre", nullable = false, unique = true, length = 20)
    private String nombre;

    @Column(name = "descripcion", length = 120)
    private String descripcion;

    /**
     * Metodo que realiza la operacion de getIdRol.
     * @return resultado de la operacion
     */
    public Short getIdRol() {
        return idRol;
    }

    /**
     * Metodo que realiza la operacion de setIdRol.
     * @param idRol parametro de entrada para la operacion
     */
    public void setIdRol(Short idRol) {
        this.idRol = idRol;
    }

    /**
     * Metodo que realiza la operacion de getNombre.
     * @return resultado de la operacion
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Metodo que realiza la operacion de setNombre.
     * @param nombre parametro de entrada para la operacion
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo que realiza la operacion de getDescripcion.
     * @return resultado de la operacion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Metodo que realiza la operacion de setDescripcion.
     * @param descripcion parametro de entrada para la operacion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
