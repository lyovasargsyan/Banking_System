package org.example.bank_system.controller;

import jakarta.validation.Valid;
import org.example.bank_system.dto.*;
import org.example.bank_system.models.Account;
import org.example.bank_system.repository.AccountRepository;
import org.example.bank_system.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request){
        return  accountService.createAccount(request);
    }


}
