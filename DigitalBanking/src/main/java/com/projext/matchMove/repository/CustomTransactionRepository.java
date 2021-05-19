package com.projext.matchMove.repository;

import com.projext.matchMove.domain.TransactionData;
import com.projext.matchMove.model.Transaction;

import java.util.List;

public interface CustomTransactionRepository {

    List<Transaction> findTransactions(TransactionData transactionData);
}
