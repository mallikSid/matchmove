package com.projext.matchMove.service.impl;

import com.projext.matchMove.domain.AccountInformation;
import com.projext.matchMove.model.Account;
import com.projext.matchMove.model.Customer;
import com.projext.matchMove.model.CustomerAccountXRef;
import com.projext.matchMove.repository.AccountRepository;
import com.projext.matchMove.repository.CustomerAccountXRefRepository;
import com.projext.matchMove.repository.CustomerRepository;
import com.projext.matchMove.service.AccountService;
import com.projext.matchMove.service.helper.BankingServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    @Autowired
    private CustomerAccountXRefRepository custAccXRefRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BankingServiceHelper bankingServiceHelper;
    @Autowired
    private CustomerRepository customerRepository;


    /**
     * Find Account
     *
     */
    public ResponseEntity<Object> findByAccountNumber(Long accountNumber) {

        Optional<Account> accountEntityOpt = accountRepository.findByAccountNumber(accountNumber);

        if(accountEntityOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.FOUND).body(bankingServiceHelper.convertToAccountDomain(accountEntityOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Number " + accountNumber + " not found.");
        }

    }

    /**
     * Create new account
     *
     */
    public ResponseEntity<Object> addNewAccount(AccountInformation accountInformation, Long customerNumber) {

        Optional<Customer> customerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);

        if(customerEntityOpt.isPresent()) {
            accountRepository.save(bankingServiceHelper.convertToAccountEntity(accountInformation));

            // Add an entry to the CustomerAccountXRef
            custAccXRefRepository.save(CustomerAccountXRef.builder()
                    .accountNumber(accountInformation.getAccountNumber())
                    .customerNumber(customerNumber)
                    .build());

        }

        return ResponseEntity.status(HttpStatus.CREATED).body("New Account created successfully.");
    }

}
