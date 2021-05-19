package com.projext.matchMove.controller;

import com.projext.matchMove.domain.AccountInformation;
import com.projext.matchMove.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("accounts")
@Api(tags = { "Accounts REST endpoints" })
public class AccountController {

	@Autowired
	private AccountService accountService;

	@GetMapping(path = "/{accountNumber}")
	@ApiOperation(value = "Get account details", notes = "Find account details by account number")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<Object> getByAccountNumber(@PathVariable Long accountNumber) {

		return accountService.findByAccountNumber(accountNumber);
	}

	@PostMapping(path = "/add/{customerNumber}")
	@ApiOperation(value = "Add a new account", notes = "Create an new account for existing customer.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<Object> addNewAccount(@RequestBody AccountInformation accountInformation,
                                                @PathVariable Long customerNumber) {

		return accountService.addNewAccount(accountInformation, customerNumber);
	}
}
