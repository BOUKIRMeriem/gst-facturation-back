package com.facturation.backend_facturation.services.implementation;

import com.facturation.backend_facturation.document.ClientDocument;
import com.facturation.backend_facturation.document.FactureClientDocument;
import com.facturation.backend_facturation.document.PaymentClientDocument;
import com.facturation.backend_facturation.exceptions.exception.ResourceNotFoundException;
import com.facturation.backend_facturation.repository.ClientRepository;
import com.facturation.backend_facturation.repository.FactureClientRepository;
import com.facturation.backend_facturation.repository.PaymentClientRepository;
import com.facturation.backend_facturation.services.PaymentClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentClientService {

    @Autowired
    private PaymentClientRepository paymentClientRepository;

    @Autowired
    private FactureClientRepository factureClientRepository;

    @Override
    public List<PaymentClientDocument> getAll(String clientId) {
        List<PaymentClientDocument> list = new ArrayList<>();
        if(clientId != null && !"null".equalsIgnoreCase(clientId) && !"undefined".equalsIgnoreCase(clientId)){
            list = paymentClientRepository.findAllByClient_Id(clientId);
        } else {
            list = paymentClientRepository.findAll();
        }
        return list;
    }

    @Override
    public PaymentClientDocument verifiedPayment(String paymentId) {
        // find a payment by its id and throw an exception if the payment is not found
        PaymentClientDocument paymentClient = paymentClientRepository.findById(paymentId).orElseThrow(()->
                new ResourceNotFoundException("This payment not found with id {} " + paymentId));
        paymentClient.setVerified(true);
        return paymentClientRepository.save(paymentClient);
    }

    @Override
    public PaymentClientDocument unVerifiedPayment(String paymentId) {
        // find a payment by its id and throw an exception if the payment is not found
        PaymentClientDocument paymentClient = paymentClientRepository.findById(paymentId).orElseThrow(()->
                new ResourceNotFoundException("This payment not found with id {} " + paymentId));
        paymentClient.setVerified(false);
        return paymentClientRepository.save(paymentClient);
    }

    @Override
    public List<PaymentClientDocument> getAllByFacture(String factureId) {
        List<PaymentClientDocument> list = new ArrayList<>();
        FactureClientDocument facture = factureClientRepository.findById(factureId).orElse(null);
        if (facture != null){
            list = paymentClientRepository.findAllByFactureClient_Id(factureId);
        }
        return list;
    }

    @Override
    public PaymentClientDocument save(PaymentClientDocument payment) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(payment.getDate());
        payment.setYear(calendar.get(Calendar.YEAR));
        return paymentClientRepository.save(payment);
    }

    @Override
    public PaymentClientDocument update(PaymentClientDocument payment, String paymentId) {
        PaymentClientDocument paymentClient = getById(paymentId);
        paymentClient
                .setDate(payment.getDate())
                .setAmount(payment.getAmount())
                .setType(payment.getType())
                .setVerified(false)
                .setClient(payment.getClient())
                .setBank(payment.getBank())
                .setTier(payment.getTier())
                .setDeadline(payment.getDeadline())
                .setDesignation(payment.getDesignation()) ;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(payment.getDate());
        paymentClient.setYear(calendar.get(Calendar.YEAR));
        return paymentClientRepository.save(paymentClient);
    }

    @Override
    public PaymentClientDocument getById(String paymentId) {
        // find a payment by its id and throw an exception if the payment is not found
        PaymentClientDocument paymentClient = paymentClientRepository.findById(paymentId).orElseThrow(()->
                new ResourceNotFoundException("This payment not found with id {} " + paymentId));
        return paymentClientRepository.save(paymentClient);
    }

    @Override
    public PaymentClientDocument deleteById(String paymentId) {
        // find a payment by its id and throw an exception if the payment is not found
        PaymentClientDocument paymentClient = getById(paymentId);
        if(!paymentClient.isVerified()){
            paymentClientRepository.delete(paymentClient);
        }
        return paymentClient;
    }
}
