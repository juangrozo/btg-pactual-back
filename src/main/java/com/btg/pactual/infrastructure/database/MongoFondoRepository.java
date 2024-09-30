package com.btg.pactual.infrastructure.database;

import com.btg.pactual.domain.model.Fondo;
import com.btg.pactual.domain.ports.FondoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MongoFondoRepository implements FondoRepository {

    private final SpringDataMongoFondoRepository springDataRepo;

    @Autowired
    public MongoFondoRepository(SpringDataMongoFondoRepository springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    @Override
    public Optional<Fondo> findById(String id) {
        return springDataRepo.findById(id);
    }

    @Override
    public void save(Fondo fondo) {
        springDataRepo.save(fondo);
    }
}
