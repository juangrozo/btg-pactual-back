package com.btg.pactual.infrastructure.database;

import com.btg.pactual.domain.model.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringDataMongoTransaccionRepository extends MongoRepository<Transaccion, String> {
    List<Transaccion> findByIdCliente(String idCliente);
}
