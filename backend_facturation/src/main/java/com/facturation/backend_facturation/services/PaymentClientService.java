package com.facturation.backend_facturation.services;


import com.facturation.backend_facturation.document.PaymentClientDocument;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentClientService {

    List<PaymentClientDocument> getAll(String clientId);

    PaymentClientDocument verifiedPayment(String paymentId);

    PaymentClientDocument unVerifiedPayment(String paymentId);

    List<PaymentClientDocument> getAllByFacture(String factureId);

    PaymentClientDocument save(PaymentClientDocument payment);

    PaymentClientDocument update(PaymentClientDocument payment, String paymentId);

    PaymentClientDocument getById(String paymentId);

    PaymentClientDocument deleteById(String paymentId);
}
