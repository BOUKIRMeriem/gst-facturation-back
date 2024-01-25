package com.facturation.backend_facturation.controller;

import com.facturation.backend_facturation.document.ClientDocument;
import com.facturation.backend_facturation.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.facturation.backend_facturation.exceptions.exception.ResourceNotFoundException;
import org.springframework.web.server.ResponseStatusException;
import com.facturation.backend_facturation.exceptions.exception.AlreadyExistsException;

import java.util.List;

@Slf4j
@RequestMapping("/api/client/")
@RestController
@CrossOrigin("*")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDocument>> getAllClients(){
        try{
            log.info("Request for getting all the clients");
            return new ResponseEntity<>(clientService.getAllClients(), HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
    @PostMapping
    public ResponseEntity<ClientDocument> createClient(@RequestBody ClientDocument client){
        try {
            log.info("Request for adding new client");
            return new ResponseEntity<>(clientService.saveClient(client),HttpStatus.CREATED);
        }catch (AlreadyExistsException ex){
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
    @GetMapping("{clientId}")
    public ResponseEntity<ClientDocument> getClientById(@PathVariable("clientId") String clientId){
        try {
            log.info("Request for getting client {} "+clientId);
            ClientDocument client = clientService.getClientById(clientId);
            return new ResponseEntity<>(client,HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
    @PutMapping("{clientId}")
    public ResponseEntity<ClientDocument> updateClient(@PathVariable("clientId") String clientId , @RequestBody ClientDocument client){
        try {
            log.info("Request for updating client");
            return new ResponseEntity<>(clientService.updateClient(client,clientId),HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }catch (AlreadyExistsException ex){
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
    @DeleteMapping("delete/{clientId}")
    public ResponseEntity<ClientDocument> deleteClient(@PathVariable("clientId") String clientId){
        try {
            log.info("Request for deleting client");
            return new ResponseEntity<>(clientService.deleteClientById(clientId),HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
    @GetMapping("count")
    public long countClient(){
        return clientService.countClient();

    }

}
