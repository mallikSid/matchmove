package com.projext.matchMove.repository;

import com.projext.matchMove.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String>,CustomTransactionRepository {

    Optional<List<Transaction>> findByAccountNumber(Long accountNumber);
    
}
