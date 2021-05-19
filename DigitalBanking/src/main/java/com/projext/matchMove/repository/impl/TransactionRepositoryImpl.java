package com.projext.matchMove.repository.impl;

import com.projext.matchMove.domain.TransactionData;
import com.projext.matchMove.model.Transaction;
import com.projext.matchMove.repository.CustomTransactionRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements CustomTransactionRepository {

    EntityManager em;

    @Override
    public List<Transaction> findTransactions(TransactionData transactionData) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);

        Root<Transaction> trnx = cq.from(Transaction.class);
        List<Predicate> predicates = new ArrayList<>();

        if (transactionData.getTxType() != null) {
            predicates.add(cb.equal(trnx.get("txType"), transactionData.getTxType()));
        }
        if (transactionData.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(trnx.get("txDateTime"),transactionData.getStartDate()));
        }
        if (transactionData.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(trnx.get("txDateTime"),transactionData.getEndDate()));
        }
        if (transactionData.getTxAmount() != null) {
            predicates.add(cb.lessThanOrEqualTo(trnx.get("txAmount"),transactionData.getTxAmount()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
