package com.facturation.backend_facturation.services.implementation;


import com.facturation.backend_facturation.document.Account;
import com.facturation.backend_facturation.document.User;
import com.facturation.backend_facturation.exceptions.exception.ResourceNotFoundException;
import com.facturation.backend_facturation.repository.AccountRepository;
import com.facturation.backend_facturation.repository.UserRepository;
import com.facturation.backend_facturation.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceImplementation implements AccountService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public Account saveAccount(com.facturation.backend_facturation.controller.requests.NewAccountRequest accountRequest) {
        log.info("Saving new user {} to the database!!", accountRequest.getEmail());
        User user = new User().builder()
                .userName(accountRequest.getUser().getUserName())
                .firstName(accountRequest.getUser().getFirstName())
                .lastName(accountRequest.getUser().getLastName())
                .phoneNumber(accountRequest.getUser().getPhoneNumber()).build();
        User user1 = userRepository.save(user);
        Account account = new Account().builder()
                .email(accountRequest.getEmail())
                .password(encoder.encode(accountRequest.getPassword()))
                .enabled(true)
                .verified(true)
                .user(user1)
                .build();
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(com.facturation.backend_facturation.controller.requests.UpdateAccountRequest accountRequest, String accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Account not found with id {} for update " + accountId));
        User user = userRepository.findById(account.getUser().getId()).orElseThrow(()-> new ResourceNotFoundException("User not found with id {} for update " + account.getUser().getId()));
        user.setUserName(accountRequest.getUser().getUserName())
                .setFirstName(accountRequest.getUser().getFirstName())
                .setLastName(accountRequest.getUser().getLastName())
                .setPhoneNumber(accountRequest.getUser().getPhoneNumber());
        // User user1 = userRepository.save(user);
        account.setUser(user);
        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(String email) {
        log.info("Fetching account by email {}", email);
        return accountRepository.getAccountByEmail(email);
    }

    @Override
    public void deleteAccount(String accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Account not found with id {} for delete "+accountId));
        accountRepository.delete(account);
    }

    @Override
    public Account getAccountById(String id) {
        // find account by its id and throw an exception if not found
        Account account = accountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Account not found with id {} "+id));
        // convert the account to an AccountDto object and return it
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        log.info("Fetching all users ");
        return accountRepository.findAll();
    }


}
