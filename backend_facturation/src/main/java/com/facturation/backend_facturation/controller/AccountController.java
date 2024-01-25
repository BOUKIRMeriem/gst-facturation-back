package com.facturation.backend_facturation.controller;


import com.facturation.backend_facturation.controller.requests.NewAccountRequest;
import com.facturation.backend_facturation.controller.requests.UpdateAccountRequest;
import com.facturation.backend_facturation.document.Account;
import com.facturation.backend_facturation.exceptions.exception.AlreadyExistsException;
import com.facturation.backend_facturation.exceptions.exception.ResourceNotFoundException;
import com.facturation.backend_facturation.security.utils.JwtUtil;
import com.facturation.backend_facturation.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account/")
@Slf4j
public class AccountController {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final AccountService accountService;
    @GetMapping(value = "token/refresh", produces = {"application/json"})
    public void generateNewAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = jwtUtil.refreshTokenVerifier(request, response);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        jwtUtil.generateAccessTokenWhenExpired(request, response, userDetails);
    }

    @GetMapping(value = "all")
    public ResponseEntity<List<Account>> getAll(){
        log.info("Request for getting all the accounts");
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @PostMapping(value = "register", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<?> register(@RequestBody NewAccountRequest accountRequest){
        try {
            return new ResponseEntity<>(accountService.saveAccount(accountRequest), HttpStatus.CREATED);
        }catch (AlreadyExistsException ex){
            // log an error message if the email already exists
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a BAD_REQUEST status code and the exception message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PutMapping(value = "{accountId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<?> update(@RequestBody UpdateAccountRequest accountRequest, @PathVariable("accountId") String accountId){
        try {
            return new ResponseEntity<>(accountService.updateAccount(accountRequest,accountId),HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            // log an error message account not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a NOT_FOUND status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping(value = "{accountId}", produces = {"application/json"})
    public ResponseEntity<?> get(@PathVariable("accountId") String accountId){
        try {
            log.info("Request for getting account");
            // find the account by its id and return an OK response with the found account information
            return new ResponseEntity<>(accountService.getAccountById(accountId),HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            // log an error message if the account is not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a NOT_FOUND status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("{accountId}")
    public ResponseEntity<?> delete(@PathVariable(name = "accountId") String id){
        try {
            log.info("Request for deleting account");
            accountService.deleteAccount(id);
            return ResponseEntity.noContent().build();
        }catch (ResourceNotFoundException ex){
            // log an error message if account not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a BAD_REQUEST status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}
