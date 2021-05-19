package com.projext.matchMove.service;

import com.projext.matchMove.domain.AccountInformation;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    public ResponseEntity<Object> findByAccountNumber(Long accountNumber);

    public ResponseEntity<Object> addNewAccount(AccountInformation accountInformation, Long customerNumber);

}
