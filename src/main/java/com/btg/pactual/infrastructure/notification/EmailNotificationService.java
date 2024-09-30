package com.btg.pactual.infrastructure.notification;

import com.btg.pactual.domain.model.Cliente;
import com.btg.pactual.domain.model.Fondo;
import com.btg.pactual.domain.ports.NotificationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements NotificationPort {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${spring.mail.password}")
    private String pass;

    @Override
    public void enviarNotificacion(Cliente cliente, Fondo fondo, String tipo) {
        // Prepara el mensaje de correo
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(cliente.getCorreo());
        message.setSubject("Notificación de " + tipo);
        message.setText(crearMensaje(cliente, fondo, tipo));

        try {
            mailSender.send(message);
            System.out.println("Notificación por correo enviada al cliente: " + cliente.getNombre() + " para la acción: " + tipo);
        } catch (MailException ex) {
            System.err.println("Error al enviar correo: " + ex.getMessage());
        }
    }

    private String crearMensaje(Cliente cliente, Fondo fondo, String tipo) {
        return "Estimado/a " + cliente.getNombre() + ",\n\n" +
                "Usted ha realizado una " + tipo + " en el fondo " + fondo.getNombre() + ".\n\n" +
                "Si tiene alguna pregunta, no dude en contactarnos.\n\n" +
                "Saludos,\n" +
                "BTG Pactual";
    }
}
