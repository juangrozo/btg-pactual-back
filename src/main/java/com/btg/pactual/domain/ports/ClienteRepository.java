package com.btg.pactual.domain.ports;

import com.btg.pactual.domain.model.Cliente;

import java.util.Optional;

public interface ClienteRepository {
    Optional<Cliente> findById(String id);
    void save(Cliente cliente);
}
