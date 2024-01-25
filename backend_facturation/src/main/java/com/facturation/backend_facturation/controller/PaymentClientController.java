package com.facturation.backend_facturation.controller;


import com.facturation.backend_facturation.document.PaymentClientDocument;
import com.facturation.backend_facturation.exceptions.exception.AlreadyExistsException;
import com.facturation.backend_facturation.exceptions.exception.ResourceNotFoundException;
import com.facturation.backend_facturation.services.PaymentClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/api/payment-client/")
@RestController
@CrossOrigin("*")
@Slf4j
public class PaymentClientController {

    @Autowired
    private PaymentClientService paymentClientService;


    @GetMapping("list/{clientId}")
    public ResponseEntity<List<PaymentClientDocument>> getListPaymentClient(@PathVariable("clientId") String clientId){
        log.info("Request for getting all the payments of the client");
        // return an OK response with the list of payments
        return new ResponseEntity<>(paymentClientService.getAll(clientId), HttpStatus.OK);
    }

    @GetMapping("facture/{factureId}")
    public ResponseEntity<List<PaymentClientDocument>> getListPaymentClientByFacture(@PathVariable("factureId") String factureId){
        log.info("Request for getting all the payments of the client by facture");
        // return an OK response with the list of payments
        return new ResponseEntity<>(paymentClientService.getAllByFacture(factureId), HttpStatus.OK);
    }

    @GetMapping("{paymentId}")
    public ResponseEntity<PaymentClientDocument> getPaymentById(@PathVariable("paymentId") String paymentId){
        try {
            log.info("Request for getting payment");
            // find the payment by its id and return an OK response with the found payment information
            return new ResponseEntity<>(paymentClientService.getById(paymentId),HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            // log an error message if the payment is not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a NOT_FOUND status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping
    public ResponseEntity<PaymentClientDocument> createPayment(@RequestBody PaymentClientDocument payment){
        try {
            log.info("Request for adding new payment");
            // save the new payment and return an CREATED response with the new payment information
            return new ResponseEntity<>(paymentClientService.save(payment),HttpStatus.CREATED);
        }catch (AlreadyExistsException ex){
            // log an error message if the payment already exists
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a BAD_REQUEST status code and the exception message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PutMapping("{paymentId}")
    public ResponseEntity<PaymentClientDocument> updatePayment(@RequestBody PaymentClientDocument payment, @PathVariable("paymentId") String paymentId){
        try {
            log.info("Request for updating payment {} ");
            // update payment and return an CREATED response with the new payment information
            return new ResponseEntity<>(paymentClientService.update(payment,paymentId),HttpStatus.CREATED);
        }catch (ResourceNotFoundException ex){
            // log an error message if the payment not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a NOT_FOUND status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("{paymentId}")
    public ResponseEntity<PaymentClientDocument> deletePayment(@PathVariable("paymentId") String paymentId){
        try {
            log.info("Request for deleting payment {} " + paymentId);
            // delete payment and return an ok response with the payment information
            return new ResponseEntity<>(paymentClientService.deleteById(paymentId),HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            // log an error message if the payment is not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a NOT_FOUND status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("verified/{paymentId}")
    public ResponseEntity<PaymentClientDocument> verified(@PathVariable("paymentId") String paymentId) {
        log.info("Request for verified payment {} " + paymentId);
        return new ResponseEntity<>(paymentClientService.verifiedPayment(paymentId),HttpStatus.OK);
    }

    @GetMapping("unverified/{paymentId}")
    public ResponseEntity<PaymentClientDocument> unVerified(@PathVariable("paymentId") String paymentId) {
        log.info("Request for unverified payment {} " + paymentId);
        return new ResponseEntity<>(paymentClientService.unVerifiedPayment(paymentId),HttpStatus.OK);
    }

}
