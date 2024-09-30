package com.btg.pactual.infrastructure.database;

import com.btg.pactual.domain.model.Fondo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataMongoFondoRepository extends MongoRepository<Fondo, String> {
}
