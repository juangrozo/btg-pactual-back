package com.btg.pactual.domain.ports;

import com.btg.pactual.domain.model.Fondo;

import java.util.Optional;

public interface FondoRepository {
    Optional<Fondo> findById(String id);
    void save(Fondo fondo);
}