package com.bambino.comprobantes.service;

import com.bambino.comprobantes.dto.ComprobantePdfResponse;
import com.bambino.comprobantes.entity.ComprobanteEntity;
import com.bambino.shared.exception.NegocioException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class ComprobanteCorreoService {

    private final ComprobantePdfService comprobantePdfService;

    @Value("${comprobantes.correo.habilitado:${seguridad.recuperacion.correo.habilitado:false}}")
    private boolean correoHabilitado;

    @Value("${comprobantes.correo.desde:${seguridad.recuperacion.correo.desde:no-reply@bambino.local}}")
    private String correoDesde;

    @Value("${comprobantes.correo.host:smtp.gmail.com}")
    private String mailHost;

    @Value("${comprobantes.correo.port:587}")
    private int mailPort;

    @Value("${comprobantes.correo.username:}")
    private String mailUsername;

    @Value("${comprobantes.correo.password:}")
    private String mailPassword;

    @Value("${comprobantes.correo.smtp-auth:true}")
    private boolean smtpAuth;

    @Value("${comprobantes.correo.starttls:true}")
    private boolean starttls;

    public ComprobanteCorreoService(ComprobantePdfService comprobantePdfService) {
        this.comprobantePdfService = comprobantePdfService;
    }

    public void enviar(ComprobanteEntity comprobante, String correoDestino) {
        if (!correoHabilitado) {
            throw new NegocioException("envio de correo de comprobante deshabilitado por configuracion");
        }
        if (correoDestino == null || correoDestino.isBlank()) {
            throw new NegocioException("cliente no tiene correo registrado");
        }
        if (mailUsername == null || mailUsername.isBlank() || mailPassword == null || mailPassword.isBlank()) {
            throw new NegocioException("smtp de comprobantes no configurado");
        }

        ComprobantePdfResponse pdf = comprobantePdfService.generarPdfAdmin(comprobante.getIdComprobante());
        JavaMailSenderImpl mailSender = crearMailSenderComprobantes();
        MimeMessage mensaje = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                mensaje,
                true,
                StandardCharsets.UTF_8.name()
            );
            helper.setFrom(correoDesde);
            helper.setTo(correoDestino.trim());
            helper.setSubject("Comprobante " + comprobante.getNumeroCompleto() + " - Bambino Chicken");
            helper.setText(textoCorreo(comprobante), false);
            helper.addAttachment(
                pdf.filename(),
                new ByteArrayResource(pdf.contenido()),
                "application/pdf"
            );
        } catch (MessagingException e) {
            throw new NegocioException("no se pudo preparar el correo del comprobante");
        }

        mailSender.send(mensaje);
    }

    private JavaMailSenderImpl crearMailSenderComprobantes() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", String.valueOf(smtpAuth));
        properties.put("mail.smtp.starttls.enable", String.valueOf(starttls));
        return mailSender;
    }

    private String textoCorreo(ComprobanteEntity comprobante) {
        return "Hola,\n\n" +
            "Adjuntamos tu comprobante " + comprobante.getNumeroCompleto() + " por tu compra en Bambino Chicken.\n\n" +
            "Gracias por tu preferencia.";
    }
}
