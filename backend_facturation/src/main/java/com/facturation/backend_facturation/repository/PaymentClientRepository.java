package com.facturation.backend_facturation.repository;

import com.facturation.backend_facturation.document.ClientDocument;
import com.facturation.backend_facturation.document.FactureClientDocument;
import com.facturation.backend_facturation.document.PaymentClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentClientRepository  extends MongoRepository<PaymentClientDocument, String> {

    void deleteAllByFactureClient_Id(String factureId);

    List<PaymentClientDocument> findAllByFactureClient_Id(String factureId);

    List<PaymentClientDocument> findAllByClient_Id(String clientId);

}
