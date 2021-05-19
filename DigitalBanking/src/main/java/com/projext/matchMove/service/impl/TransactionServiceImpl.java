package com.projext.matchMove.service.impl;

import com.projext.matchMove.domain.TransactionData;
import com.projext.matchMove.domain.TransactionDetails;
import com.projext.matchMove.domain.TransferDetails;
import com.projext.matchMove.model.Account;
import com.projext.matchMove.model.Customer;
import com.projext.matchMove.model.Transaction;
import com.projext.matchMove.repository.AccountRepository;
import com.projext.matchMove.repository.CustomerRepository;
import com.projext.matchMove.repository.TransactionRepository;
import com.projext.matchMove.service.TransactionService;
import com.projext.matchMove.service.helper.BankingServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankingServiceHelper bankingServiceHelper;
    @Autowired
    private RedisLockRegistry redisLockRegistry;


    public ResponseEntity<Object> transferDetails(TransferDetails transferDetails, Long customerNumber) {

             List<Account> accountEntities = new ArrayList<>();
        Account fromAccountEntity = null;
        Account toAccountEntity = null;

        Optional<Customer> customerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);


            // If customer is present
        if(customerEntityOpt.isPresent()) {

            Lock invoiceLock = redisLockRegistry.obtain("transfer_lock:" + customerEntityOpt.get().getCustomerNumber());
            try {
                invoiceLock.lock();
                // get FROM ACCOUNT info
                Optional<Account> fromAccountEntityOpt = accountRepository.findByAccountNumber(transferDetails.getFromAccountNumber());
                if (fromAccountEntityOpt.isPresent()) {
                    fromAccountEntity = fromAccountEntityOpt.get();
                } else {
                    // if from request does not exist, 404 Bad Request
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("From Account Number " + transferDetails.getFromAccountNumber() + " not found.");
                }


                // get TO ACCOUNT info
                Optional<Account> toAccountEntityOpt = accountRepository.findByAccountNumber(transferDetails.getToAccountNumber());
                if (toAccountEntityOpt.isPresent()) {
                    toAccountEntity = toAccountEntityOpt.get();
                } else {
                    // if from request does not exist, 404 Bad Request
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("To Account Number " + transferDetails.getToAccountNumber() + " not found.");
                }


                // if not sufficient funds, return 400 Bad Request
                if (fromAccountEntity.getAccountBalance() < transferDetails.getTransferAmount()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Funds.");
                } else {
                    // update FROM ACCOUNT
                    fromAccountEntity.setAccountBalance(fromAccountEntity.getAccountBalance() - transferDetails.getTransferAmount());
                    fromAccountEntity.setUpdateDateTime(new Date());
                    accountEntities.add(fromAccountEntity);

                    // update TO ACCOUNT
                    toAccountEntity.setAccountBalance(toAccountEntity.getAccountBalance() + transferDetails.getTransferAmount());
                    toAccountEntity.setUpdateDateTime(new Date());
                    accountEntities.add(toAccountEntity);

                    accountRepository.saveAll(accountEntities);

                    // Create transaction for FROM Account
                    Transaction fromTransaction = bankingServiceHelper.createTransaction(transferDetails, fromAccountEntity.getAccountNumber(), "DEBIT");
                    transactionRepository.save(fromTransaction);

                    // Create transaction for TO Account
                    Transaction toTransaction = bankingServiceHelper.createTransaction(transferDetails, toAccountEntity.getAccountNumber(), "CREDIT");
                    transactionRepository.save(toTransaction);

                    return ResponseEntity.status(HttpStatus.OK).body("Success: Amount transferred for Customer Number " + customerNumber);
                }
            }finally {
                try {
                    invoiceLock.unlock();
                } catch (Exception e) {
                    log.warn("invoice generation lock was autoreleased");
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Number " + customerNumber + " not found.");
        }

    }

    /**
     * Get all transactions for a specific account
     *
     */
    public List<TransactionDetails> findTransactionsByAccountNumber(Long accountNumber, TransactionData transactionData) {
        List<TransactionDetails> transactionDetails = new ArrayList<>();
        Optional<Account> accountEntityOpt = accountRepository.findByAccountNumber(accountNumber);
        if(accountEntityOpt.isPresent()) {
            List<Transaction> transactionEntitiesOpt = transactionRepository.findTransactions(transactionData);
            if(transactionEntitiesOpt.size()>0) {
                transactionEntitiesOpt.forEach(transaction -> {
                    transactionDetails.add(bankingServiceHelper.convertToTransactionDomain(transaction));
                });
            }
        }

        return transactionDetails;
    }

}
