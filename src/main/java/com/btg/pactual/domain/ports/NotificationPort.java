package com.btg.pactual.domain.ports;

import com.btg.pactual.domain.model.Cliente;
import com.btg.pactual.domain.model.Fondo;

public interface NotificationPort {
    void enviarNotificacion(Cliente cliente, Fondo fondo, String tipo);
}
