package com.btg.pactual.infrastructure.database;

import com.btg.pactual.domain.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataMongoClienteRepository extends MongoRepository<Cliente, String> {
    // Spring Data MongoDB se encarga de proporcionar las implementaciones CRUD
}
