package com.facturation.backend_facturation.repository;

import com.facturation.backend_facturation.document.ClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<ClientDocument,String> {

    boolean existsByName(String name);
    boolean existsByEmail(String email);
}

