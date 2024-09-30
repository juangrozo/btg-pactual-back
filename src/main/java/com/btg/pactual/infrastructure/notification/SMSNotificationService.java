package com.btg.pactual.infrastructure.notification;

import com.btg.pactual.domain.model.Cliente;
import com.btg.pactual.domain.model.Fondo;
import com.btg.pactual.domain.ports.NotificationPort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSNotificationService implements NotificationPort {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String fromPhoneNumber;

    @Override
    public void enviarNotificacion(Cliente cliente, Fondo fondo, String tipo) {
        // Inicializar Twilio con las credenciales
        Twilio.init(accountSid, authToken);

        // Preparar el mensaje
        String mensaje = "Estimado " + cliente.getNombre() + ", " +
                "se ha realizado una " + tipo + " en el fondo " + fondo.getNombre() + ".";

        // Enviar el mensaje
        Message.creator(
                new PhoneNumber(cliente.getTelefono()),  // Cliente debe tener un número de teléfono registrado
                new PhoneNumber(fromPhoneNumber),       // Número de Twilio
                mensaje
        ).create();
    }
}
