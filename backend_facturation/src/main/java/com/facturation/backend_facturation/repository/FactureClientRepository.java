package com.facturation.backend_facturation.repository;

import com.facturation.backend_facturation.document.FactureClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureClientRepository extends MongoRepository<FactureClientDocument, String> {

    List<FactureClientDocument> findAllByClient_Id(String clientId);
}
