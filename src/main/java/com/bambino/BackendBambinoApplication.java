package com.bambino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * Clase que maneja la funcionalidad de BackendBambinoApplication.
 */
public class BackendBambinoApplication {

    /**
     * Metodo que ejecuta el programa.
     * @param args parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendBambinoApplication.class, args);
    }
}
