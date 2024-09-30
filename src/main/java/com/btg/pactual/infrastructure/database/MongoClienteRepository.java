package com.btg.pactual.infrastructure.database;

import com.btg.pactual.domain.model.Cliente;
import com.btg.pactual.domain.ports.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MongoClienteRepository implements ClienteRepository {

    private final SpringDataMongoClienteRepository springDataRepo;

    @Autowired
    public MongoClienteRepository(SpringDataMongoClienteRepository springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    @Override
    public Optional<Cliente> findById(String id) {
        System.out.println("Vamos a buscar por ID cliente: " + id);
        Optional<Cliente> client = springDataRepo.findById(id);

        client.ifPresentOrElse(
                c -> System.out.println("Client found: " + c.getNombre() + " with ID: " + c.getId()),
                () -> System.out.println("No client found with ID: " + id)
        );

        return client;
    }

    @Override
    public void save(Cliente cliente) {
        springDataRepo.save(cliente);
    }
}
