package com.btg.pactual.domain.ports;

import com.btg.pactual.domain.model.Transaccion;

import java.util.List;

public interface TransaccionRepository {
    void save(Transaccion transaccion);
    List<Transaccion> findByIdCliente(String clienteId);
}
