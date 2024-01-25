package com.facturation.backend_facturation.repository;

import com.facturation.backend_facturation.document.Account;
import com.facturation.backend_facturation.dto.AccountBasicInfoSecurityView;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account,String> {

    Account getAccountByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Account> findAccountByEmail(String email);

    AccountBasicInfoSecurityView findByEmail(String email);
}
