package com.facturation.backend_facturation.repository;

import com.facturation.backend_facturation.document.DetailFactureClientDocument;
import com.facturation.backend_facturation.document.FactureClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailFactureClientRepository extends MongoRepository<DetailFactureClientDocument, String> {

    void deleteAllByFactureClient(FactureClientDocument facture);
}
