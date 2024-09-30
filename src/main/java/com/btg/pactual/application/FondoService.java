package com.btg.pactual.application;

import com.btg.pactual.domain.model.Cliente;
import com.btg.pactual.domain.model.Fondo;
import com.btg.pactual.domain.model.Transaccion;
import com.btg.pactual.domain.ports.ClienteRepository;
import com.btg.pactual.domain.ports.FondoRepository;
import com.btg.pactual.domain.ports.NotificationPort;
import com.btg.pactual.domain.ports.TransaccionRepository;
import com.btg.pactual.exception.InsufficientFundsException;
import com.btg.pactual.exception.ClienteNotFoundException;
import com.btg.pactual.exception.FondoNotFoundException;
import com.btg.pactual.infrastructure.notification.EmailNotificationService;
import com.btg.pactual.infrastructure.notification.SMSNotificationService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FondoService {

    private final ClienteRepository clienteRepository;
    private final FondoRepository fondoRepository;
    private final TransaccionRepository transaccionRepository;
    //private final NotificationPort notificationPort;
    private final EmailNotificationService emailNotificationService;
    private final SMSNotificationService smsNotificationService;

    public FondoService(ClienteRepository clienteRepository,
                        FondoRepository fondoRepository,
                        TransaccionRepository transaccionRepository,
                        EmailNotificationService emailNotificationService,
                        SMSNotificationService smsNotificationService) {
        this.clienteRepository = clienteRepository;
        this.fondoRepository = fondoRepository;
        this.transaccionRepository = transaccionRepository;
        this.emailNotificationService = emailNotificationService;
        this.smsNotificationService = smsNotificationService;
    }

    public void suscribirAFondo(String clienteId, String fondoId) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNotFoundException::new);
        Fondo fondo = fondoRepository.findById(fondoId).orElseThrow(FondoNotFoundException::new);

        if (cliente.getSaldo().compareTo(fondo.getMontoMinimo()) < 0) {
            throw new InsufficientFundsException("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre());
        }

        cliente.setSaldo(cliente.getSaldo().subtract(fondo.getMontoMinimo()));
        clienteRepository.save(cliente);

        Transaccion transaccion = new Transaccion();
        transaccion.setIdCliente(clienteId);
        transaccion.setIdFondo(fondoId);
        transaccion.setTipo("apertura");
        transaccion.setFecha(new Date());
        transaccion.setMonto(fondo.getMontoMinimo());
        transaccionRepository.save(transaccion);

        // Verificar la preferencia de notificación del cliente
        if ("sms".equalsIgnoreCase(cliente.getPreferenciaNotificacion())) {
            smsNotificationService.enviarNotificacion(cliente, fondo, "apertura");
        } else {
            emailNotificationService.enviarNotificacion(cliente, fondo, "apertura");
        }
    }


    public void cancelarFondo(String clienteId, String fondoId) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNotFoundException::new);
        Fondo fondo = fondoRepository.findById(fondoId).orElseThrow(FondoNotFoundException::new);

        cliente.setSaldo(cliente.getSaldo().add(fondo.getMontoMinimo()));
        clienteRepository.save(cliente);

        Transaccion transaccion = new Transaccion();
        transaccion.setIdCliente(clienteId);
        transaccion.setIdFondo(fondoId);
        transaccion.setTipo("cancelacion");
        transaccion.setFecha(new Date());
        transaccion.setMonto(fondo.getMontoMinimo());
        transaccionRepository.save(transaccion);

        // Verificar la preferencia de notificación del cliente
        if ("sms".equalsIgnoreCase(cliente.getPreferenciaNotificacion())) {
            smsNotificationService.enviarNotificacion(cliente, fondo, "cancelacion");
        } else {
            emailNotificationService.enviarNotificacion(cliente, fondo, "cancelacion");
        }
    }

    public List<Transaccion> obtenerHistorial(String clienteId) {
        return transaccionRepository.findByIdCliente(clienteId);
    }
}
