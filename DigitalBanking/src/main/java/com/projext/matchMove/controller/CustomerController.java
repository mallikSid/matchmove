package com.projext.matchMove.controller;

import com.projext.matchMove.domain.CustomerDetails;
import com.projext.matchMove.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
@Api(tags = { "Customer REST endpoints" })
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping(path = "/all")
	@ApiOperation(value = "Find all customers", notes = "Gets details of all the customers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public List<CustomerDetails> getAllCustomers() {

		return customerService.findAll();
	}

	@PostMapping(path = "/add")
	@ApiOperation(value = "Add a Customer", notes = "Add customer and create an account")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<Object> addCustomer(@RequestBody CustomerDetails customer) {

		return customerService.addCustomer(customer);
	}

	@GetMapping(path = "/{customerNumber}")
	@ApiOperation(value = "Get customer details", notes = "Get Customer details by customer number.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = CustomerDetails.class, responseContainer = "Object"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public CustomerDetails getCustomer(@PathVariable Long customerNumber) {

		return customerService.findByCustomerNumber(customerNumber);
	}

	@PutMapping(path = "/{customerNumber}")
	@ApiOperation(value = "Update customer", notes = "Update customer and any other account information associated with him.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<Object> updateCustomer(@RequestBody CustomerDetails customerDetails,
                                                 @PathVariable Long customerNumber) {

		return customerService.updateCustomer(customerDetails, customerNumber);
	}

	@DeleteMapping(path = "/{customerNumber}")
	@ApiOperation(value = "Delete customer and related accounts", notes = "Delete customer and all accounts associated with him.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<Object> deleteCustomer(@PathVariable Long customerNumber) {

		return customerService.deleteCustomer(customerNumber);
	}

}
