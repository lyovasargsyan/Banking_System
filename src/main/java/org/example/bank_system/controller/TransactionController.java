package org.example.bank_system.controller;

import jakarta.validation.Valid;
import org.example.bank_system.dto.*;
import org.example.bank_system.models.Transaction;
import org.example.bank_system.repository.TransactionRepository;
import org.example.bank_system.service.AccountService;
import org.example.bank_system.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionRepository transactionRepository, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping("/{accountNumber}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse depositMoney(@PathVariable String accountNumber,
                                        @Valid @RequestBody DepositRequest request){
        return accountService.depositMoney(accountNumber, request);
    }

    @PostMapping("/{accountNumber}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse withdrawMoney(@PathVariable String accountNumber,
                                         @Valid @RequestBody WithdrawRequest request){
        return accountService.withdrawMoney(accountNumber, request);
    }

    @PostMapping("/{senderAccountNumber}/transfer")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse transferMoney(@PathVariable String senderAccountNumber,
                                         @Valid @RequestBody TransferRequest request){
        return accountService.transferMoney(senderAccountNumber, request);
    }


    @GetMapping("/{senderAccountNumber}/transactionhistory")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> transactionsHistory(@PathVariable String senderAccountNumber,
                                                 @Valid @RequestBody TransactionsHistoryRequest request){
        return  transactionService.transactionHistory(senderAccountNumber, request);
    }
}
