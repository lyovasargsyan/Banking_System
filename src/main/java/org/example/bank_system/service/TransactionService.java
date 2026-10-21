package org.example.bank_system.service;

import jakarta.transaction.Transactional;
import org.example.bank_system.dto.TransactionsHistoryRequest;
import org.example.bank_system.models.Account;
import org.example.bank_system.models.Currency;
import org.example.bank_system.models.Transaction;
import org.example.bank_system.models.TransactionType;
import org.example.bank_system.repository.AccountRepository;
import org.example.bank_system.repository.TransactionRepository;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }





    @Transactional
    public void recordDeposit(Account account, BigDecimal amount, Currency currency){
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setReceiverAccountNumber(account.getAccountNumber());
        transaction.setCurrency(currency);
        transactionRepository.save(transaction);
    }

    @Transactional
    public void recordWithdraw(Account account, BigDecimal amount, Currency currency){
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setSenderAccountNumber(account.getAccountNumber());
        transaction.setCurrency(currency);

        transactionRepository.save(transaction);
    }

    @Transactional
    public void recordTransfer(Account senderAccount, Account receiverAccount, BigDecimal amount, Currency currency){
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSenderAccountNumber(senderAccount.getAccountNumber());
        transaction.setReceiverAccountNumber(receiverAccount.getAccountNumber());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setCurrency(currency);

        transactionRepository.save(transaction);
    }

    @Transactional
    public List<Transaction> transactionHistory(String accountNumber, TransactionsHistoryRequest request){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
                );

        if(Objects.equals(request.getPinCode(), account.getPinCode())) {
            return transactionRepository.getTransactionsBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc(account.getAccountNumber(), account.getAccountNumber());
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "PIN is wrong."
            );
        }
    }

}
