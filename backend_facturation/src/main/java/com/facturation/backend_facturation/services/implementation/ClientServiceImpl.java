package com.facturation.backend_facturation.services.implementation;

import com.facturation.backend_facturation.document.ClientDocument;
import com.facturation.backend_facturation.repository.ClientRepository;
import com.facturation.backend_facturation.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.facturation.backend_facturation.exceptions.exception.AlreadyExistsException;
import com.facturation.backend_facturation.exceptions.exception.ResourceNotFoundException;

import java.util.List;
@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<ClientDocument> getAllClients() {
        return clientRepository.findAll();
    }
    @Override
    public ClientDocument saveClient(ClientDocument client) {
        log.info("Saving new client {} to the database!!");
        if(clientRepository.existsByName(client.getName())){
            throw new AlreadyExistsException("Client with this same name already exists");
        }
        if(clientRepository.existsByEmail(client.getEmail())){
            throw new AlreadyExistsException("Client with this same email already exists");
        }
        ClientDocument clientEntity = clientRepository.save(client);
        return clientEntity;
    }
    @Override
    public ClientDocument getClientById(String id) {
        ClientDocument client = clientRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Client not found with id {} "+id));
        return client;
    }
    @Override
    public ClientDocument updateClient(ClientDocument client, String clientId) {

        ClientDocument client1 = clientRepository.findById(clientId).orElseThrow(()->
                new ResourceNotFoundException("Client not found with id {} "+clientId));
        client1
                .setCode(client.getCode())
                .setName(client.getName())
                .setIce(client.getIce())
                .setAddress(client.getAddress())
                .setEmail(client.getEmail())
                .setPhone(client.getPhone())
                .setSolde(client.getSolde());
        return clientRepository.save(client1);
    }
    @Override
    public ClientDocument deleteClientById(String clientId) {
        ClientDocument client = clientRepository.findById(clientId).orElseThrow(()->
                new ResourceNotFoundException("Client not found with id {} "+clientId));
        clientRepository.delete(client);
        return client;
    }
    @Override
    public long countClient(){
        return clientRepository.count();
    }
}
