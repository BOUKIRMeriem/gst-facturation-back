package com.facturation.backend_facturation.services;

import com.facturation.backend_facturation.document.Account;

import java.util.List;

public interface AccountService {

    Account saveAccount(com.facturation.backend_facturation.controller.requests.NewAccountRequest account);

    Account updateAccount(com.facturation.backend_facturation.controller.requests.UpdateAccountRequest account, String accountId);

    Account getAccount(String email);

    void deleteAccount(String accountId);

    Account getAccountById(String id);

    List<Account> getAllAccounts();
}
