package com.projext.matchMove.repository;

import com.projext.matchMove.model.CustomerAccountXRef;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAccountXRefRepository extends CrudRepository<CustomerAccountXRef, String> {

}
