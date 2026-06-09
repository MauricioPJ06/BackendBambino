package com.bambino.seguridad.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
/**
 * Clase que maneja la funcionalidad de CorreoRecuperacionService.
 */
public class CorreoRecuperacionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorreoRecuperacionService.class);

    private final JavaMailSender javaMailSender;

    @Value("${seguridad.recuperacion.correo.habilitado:false}")
    private boolean correoHabilitado;

    @Value("${seguridad.recuperacion.correo.desde:no-reply@bambino.local}")
    private String correoDesde;

    public CorreoRecuperacionService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Metodo que realiza la operacion de enviarCodigoRecuperacion.
     * @param correoDestino parametro de entrada para la operacion
     * @param codigo parametro de entrada para la operacion
     */
    public void enviarCodigoRecuperacion(String correoDestino, String codigo) {
        if (!correoHabilitado) {
            LOGGER.warn("envio de correo de recuperacion deshabilitado por configuracion");
            return;
        }

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(correoDesde);
        mensaje.setTo(correoDestino);
        mensaje.setSubject("codigo de recuperacion - bambino chicken");
        mensaje.setText(
            "tu codigo de recuperacion es: " + codigo + "\n" +
            "este codigo expira en 15 minutos.\n" +
            "si no solicitaste este cambio, ignora este mensaje."
        );

        javaMailSender.send(mensaje);
    }
}
