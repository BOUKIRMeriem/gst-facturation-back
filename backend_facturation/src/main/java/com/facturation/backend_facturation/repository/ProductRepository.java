package com.facturation.backend_facturation.repository;

import com.facturation.backend_facturation.document.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<ProductDocument, String> {
}
