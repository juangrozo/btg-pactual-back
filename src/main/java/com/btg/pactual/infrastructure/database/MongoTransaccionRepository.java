package com.btg.pactual.infrastructure.database;

import com.btg.pactual.domain.model.Transaccion;
import com.btg.pactual.domain.ports.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoTransaccionRepository implements TransaccionRepository {

    private final SpringDataMongoTransaccionRepository springDataRepo;

    @Autowired
    public MongoTransaccionRepository(SpringDataMongoTransaccionRepository springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    @Override
    public void save(Transaccion transaccion) {
        springDataRepo.save(transaccion);
    }

    @Override
    public List<Transaccion> findByIdCliente(String idCliente) {
        return springDataRepo.findByIdCliente(idCliente);
    }
}
