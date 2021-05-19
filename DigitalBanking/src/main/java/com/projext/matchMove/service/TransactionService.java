package com.projext.matchMove.service;

import com.projext.matchMove.domain.TransactionData;
import com.projext.matchMove.domain.TransactionDetails;
import com.projext.matchMove.domain.TransferDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransactionService {

    public ResponseEntity<Object> transferDetails(TransferDetails transferDetails, Long customerNumber);

    public List<TransactionDetails> findTransactionsByAccountNumber(Long accountNumber, TransactionData transactionData);

}
