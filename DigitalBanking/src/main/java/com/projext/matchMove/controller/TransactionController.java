package com.projext.matchMove.controller;

import com.projext.matchMove.domain.TransactionData;
import com.projext.matchMove.domain.TransactionDetails;
import com.projext.matchMove.domain.TransferDetails;
import com.projext.matchMove.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accounts")
@Api(tags = { "Transactions REST endpoints" })
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PutMapping(path = "/transfer/{customerNumber}")
    @ApiOperation(value = "Transfer funds between accounts", notes = "Transfer funds between accounts.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    public ResponseEntity<Object> transferDetails(@RequestBody TransferDetails transferDetails,
                                                  @PathVariable Long customerNumber) {

        return transactionService.transferDetails(transferDetails, customerNumber);
    }

    @GetMapping(path = "/transactions/{accountNumber}")
    @ApiOperation(value = "Get all transactions", notes = "Get all Transactions by account number")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    public List<TransactionDetails> getTransactionByAccountNumber(@PathVariable Long accountNumber,
                                                                  @RequestBody TransactionData transactionData) {

        return transactionService.findTransactionsByAccountNumber(accountNumber,transactionData);
    }
}
