package com.facturation.backend_facturation.security.userDetails;

import com.facturation.backend_facturation.dto.AccountBasicInfoSecurityView;
import com.facturation.backend_facturation.document.Account;
import com.facturation.backend_facturation.dto.AccountBasicInfoSecurityViewWithTokens;
import com.facturation.backend_facturation.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Account> accountOptional = accountRepository.findAccountByEmail(email);
        Account account;
        if(accountOptional.isEmpty()){
            log.info("User {} not found in the database", email);
            throw new UsernameNotFoundException("User not found in the database");
        }else{
            log.info("User {} found in the database", email);
            account = accountOptional.get();
        }
        com.facturation.backend_facturation.security.userDetails.UserDetails customUserDetails = new com.facturation.backend_facturation.security.userDetails.UserDetails(account);
        return new org.springframework.security.core.userdetails.User(customUserDetails.getUsername(), customUserDetails.getPassword(), customUserDetails.getAuthorities());
    }

    public AccountBasicInfoSecurityViewWithTokens loadUsersBasicInfo(String email){
        AccountBasicInfoSecurityView account = accountRepository.findByEmail(email);
        return AccountBasicInfoSecurityViewWithTokens.builder()
                .id(account.getId())
                .email(account.getEmail())
                .user(account.getUser())
                .verified(account.isVerified())
                .build();
    }
}
