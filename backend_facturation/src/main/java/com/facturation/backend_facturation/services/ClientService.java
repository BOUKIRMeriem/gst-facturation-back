package com.facturation.backend_facturation.services;

import com.facturation.backend_facturation.document.ClientDocument;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientService {
    List<ClientDocument> getAllClients();
    ClientDocument saveClient(ClientDocument client);
    ClientDocument getClientById(String id);
    ClientDocument updateClient(ClientDocument client, String clientId);
    ClientDocument deleteClientById(String clientId);
    long countClient();
}
