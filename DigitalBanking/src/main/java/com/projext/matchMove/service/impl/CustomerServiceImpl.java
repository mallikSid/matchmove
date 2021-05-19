package com.projext.matchMove.service.impl;

import com.projext.matchMove.domain.CustomerDetails;
import com.projext.matchMove.model.Address;
import com.projext.matchMove.model.Contact;
import com.projext.matchMove.model.Customer;
import com.projext.matchMove.repository.CustomerRepository;
import com.projext.matchMove.service.CustomerService;
import com.projext.matchMove.service.helper.BankingServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BankingServiceHelper bankingServiceHelper;

    public List<CustomerDetails> findAll() {

        List<CustomerDetails> allCustomerDetails = new ArrayList<>();

        Iterable<Customer> customerList = customerRepository.findAll();

        customerList.forEach(customer -> {
            allCustomerDetails.add(bankingServiceHelper.convertToCustomerDomain(customer));
        });

        return allCustomerDetails;
    }

    /**
     * CREATE Customer
     *
     */
    public ResponseEntity<Object> addCustomer(CustomerDetails customerDetails) {

        Customer customer = bankingServiceHelper.convertToCustomerEntity(customerDetails);
        customer.setCreateDateTime(new Date());
        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body("New Customer created successfully.");
    }

    /**
     * READ Customer
     *
     */

    public CustomerDetails findByCustomerNumber(Long customerNumber) {

        Optional<Customer> customerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);

        if(customerEntityOpt.isPresent())
            return bankingServiceHelper.convertToCustomerDomain(customerEntityOpt.get());

        return null;
    }

    /**
     * UPDATE Customer
     *
     */
    public ResponseEntity<Object> updateCustomer(CustomerDetails customerDetails, Long customerNumber) {
        Optional<Customer> managedCustomerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);
        Customer unmanagedCustomerEntity = bankingServiceHelper.convertToCustomerEntity(customerDetails);
        if(managedCustomerEntityOpt.isPresent()) {
            Customer managedCustomerEntity = managedCustomerEntityOpt.get();

            if(Optional.ofNullable(unmanagedCustomerEntity.getContactDetails()).isPresent()) {

                Contact managedContact = managedCustomerEntity.getContactDetails();
                if(managedContact != null) {
                    managedContact.setEmailId(unmanagedCustomerEntity.getContactDetails().getEmailId());
                    managedContact.setHomePhone(unmanagedCustomerEntity.getContactDetails().getHomePhone());
                    managedContact.setWorkPhone(unmanagedCustomerEntity.getContactDetails().getWorkPhone());
                } else
                    managedCustomerEntity.setContactDetails(unmanagedCustomerEntity.getContactDetails());
            }

            if(Optional.ofNullable(unmanagedCustomerEntity.getCustomerAddress()).isPresent()) {

                Address managedAddress = managedCustomerEntity.getCustomerAddress();
                if(managedAddress != null) {
                    managedAddress.setAddress(unmanagedCustomerEntity.getCustomerAddress().getAddress());
                    managedAddress.setCity(unmanagedCustomerEntity.getCustomerAddress().getCity());
                    managedAddress.setState(unmanagedCustomerEntity.getCustomerAddress().getState());
                    managedAddress.setZip(unmanagedCustomerEntity.getCustomerAddress().getZip());
                    managedAddress.setCountry(unmanagedCustomerEntity.getCustomerAddress().getCountry());
                } else
                    managedCustomerEntity.setCustomerAddress(unmanagedCustomerEntity.getCustomerAddress());
            }

            managedCustomerEntity.setUpdateDateTime(new Date());
            managedCustomerEntity.setStatus(unmanagedCustomerEntity.getStatus());
            managedCustomerEntity.setFirstName(unmanagedCustomerEntity.getFirstName());
            managedCustomerEntity.setMiddleName(unmanagedCustomerEntity.getMiddleName());
            managedCustomerEntity.setLastName(unmanagedCustomerEntity.getLastName());
            managedCustomerEntity.setUpdateDateTime(new Date());

            customerRepository.save(managedCustomerEntity);

            return ResponseEntity.status(HttpStatus.OK).body("Success: Customer updated.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Number " + customerNumber + " not found.");
        }
    }

    /**
     * DELETE Customer
     *
     */
    public ResponseEntity<Object> deleteCustomer(Long customerNumber) {

        Optional<Customer> managedCustomerEntityOpt = customerRepository.findByCustomerNumber(customerNumber);

        if(managedCustomerEntityOpt.isPresent()) {
            Customer managedCustomerEntity = managedCustomerEntityOpt.get();
            customerRepository.delete(managedCustomerEntity);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Customer deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer does not exist.");
        }
    }

}
