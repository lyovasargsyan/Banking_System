package org.example.bank_system.service;

import jakarta.transaction.Transactional;
import org.example.bank_system.dto.*;
import org.example.bank_system.models.Account;
import org.example.bank_system.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Objects;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request){
        Account account = new Account();
        account.setOwnerName(request.getOwnerName());
        account.setPinCode(request.getPinCode());
        account.setCurrency(request.getCurrency());
        account.setBalance(request.getInitialBalance());

        String accountNumber;

        do{
            accountNumber = String.format("%010d", secureRandom.nextLong(1_000_000_000L, 10_000_000_000L));
        }while (accountRepository.existsByAccountNumber(accountNumber));

        account.setAccountNumber(accountNumber);

        Account savedAccount = accountRepository.save(account);


        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getOwnerName(),
                savedAccount.getAccountNumber(),
                savedAccount.getBalance(),
                savedAccount.getCurrency()
        );
    }

    @Transactional
    public AccountResponse depositMoney(String accountNumber, DepositRequest request){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account with this account number not found."
                ));


        account.setBalance(
                account.getBalance().add(request.getAmount().multiply(request.getCurrency().getValue().divide(account.getCurrency().getValue())))
        );

        Account savedAccount = accountRepository.save(account);

        transactionService.recordDeposit(savedAccount, request.getAmount(), request.getCurrency());

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getOwnerName(),
                savedAccount.getAccountNumber(),
                savedAccount.getBalance(),
                savedAccount.getCurrency()
        );
    }

    @Transactional
    public AccountResponse withdrawMoney(String accountNumber, WithdrawRequest request){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account with this account number not found."
                ));
        BigDecimal convertedAmount = request.getAmount().multiply(request.getCurrency().getValue().divide(account.getCurrency().getValue()));
        if(Objects.equals(request.getPinCode(), account.getPinCode())) {
            if(account.getBalance().compareTo(convertedAmount) >= 0) {
                account.setBalance(
                        account.getBalance().subtract(convertedAmount)
                );
            }
            else{
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Insufficient funds."
                );
            }
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "PIN is wrong."
            );
        }
        Account savedAccount = accountRepository.save(account);

        transactionService.recordWithdraw(savedAccount, request.getAmount(), request.getCurrency());

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getOwnerName(),
                savedAccount.getAccountNumber(),
                savedAccount.getBalance(),
                savedAccount.getCurrency()
        );
    }

    @Transactional
    public AccountResponse transferMoney(String senderAccountNumber, TransferRequest request){
        Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account with this sender account number not found."
                ));
        Account receiverAccount = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account with this receiver account number not found."
                ));

        BigDecimal convertedAmountForSender = request.getAmount().multiply(request.getCurrency().getValue().divide(senderAccount.getCurrency().getValue()));
        BigDecimal convertedAmountForReceiver = request.getAmount().multiply(request.getCurrency().getValue().divide(receiverAccount.getCurrency().getValue()));

        if (senderAccount.getAccountNumber()
                .equals(receiverAccount.getAccountNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot transfer money to the same account."
            );
        }



        if(Objects.equals(request.getPinCode(), senderAccount.getPinCode())) {
            if(senderAccount.getBalance().compareTo(request.getAmount()) >= 0) {
                senderAccount.setBalance(
                        senderAccount.getBalance().subtract(convertedAmountForSender)
                );
                receiverAccount.setBalance(receiverAccount.getBalance().add(convertedAmountForReceiver));
            }
            else{
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Insufficient funds."
                );
            }
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "PIN is wrong."
            );
        }

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        transactionService.recordTransfer(senderAccount, receiverAccount, request.getAmount(), request.getCurrency());

        return new AccountResponse(
                senderAccount.getId(),
                senderAccount.getOwnerName(),
                senderAccount.getAccountNumber(),
                senderAccount.getBalance(),
                senderAccount.getCurrency()
        );

    }
}
