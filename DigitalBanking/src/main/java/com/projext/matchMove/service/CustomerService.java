package com.projext.matchMove.service;

import com.projext.matchMove.domain.CustomerDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {

    public List<CustomerDetails> findAll();

    public ResponseEntity<Object> addCustomer(CustomerDetails customerDetails);

    public CustomerDetails findByCustomerNumber(Long customerNumber);

    public ResponseEntity<Object> updateCustomer(CustomerDetails customerDetails, Long customerNumber);

    public ResponseEntity<Object> deleteCustomer(Long customerNumber) ;
}
