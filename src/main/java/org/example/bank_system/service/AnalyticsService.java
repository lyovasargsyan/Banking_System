package org.example.bank_system.service;

import org.example.bank_system.dto.AnalyticsRequest;
import org.example.bank_system.dto.AnalyticsResponse;
import org.example.bank_system.models.Account;
import org.example.bank_system.models.Transaction;
import org.example.bank_system.models.TransactionType;
import org.example.bank_system.repository.AccountRepository;
import org.example.bank_system.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class AnalyticsService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public AnalyticsService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public AnalyticsResponse getAnalitics(String accountNumber, AnalyticsRequest request){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
                );

        if(!Objects.equals(request.getPinCode(), account.getPinCode())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "PIN is wrong."
            );
        }


        List<Transaction> transactions = transactionRepository.getTransactionsBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc(accountNumber, accountNumber);
        BigDecimal totalDepositAMD = BigDecimal.valueOf(0);
        BigDecimal totalDepositUSD = BigDecimal.valueOf(0);
        BigDecimal totalDepositEUR = BigDecimal.valueOf(0);

        BigDecimal totalWithdrawAMD = BigDecimal.valueOf(0);
        BigDecimal totalWithdrawUSD = BigDecimal.valueOf(0);
        BigDecimal totalWithdrawEUR = BigDecimal.valueOf(0);

        BigDecimal totalTransferAMD = BigDecimal.valueOf(0);
        BigDecimal totalTransferUSD = BigDecimal.valueOf(0);
        BigDecimal totalTransferEUR = BigDecimal.valueOf(0);
        int transactionCounts = 0;

        for(Transaction transaction: transactions){
            if(transaction.getTransactionType() == TransactionType.DEPOSIT){
                switch (transaction.getCurrency()){
                    case AMD -> totalDepositAMD = totalDepositAMD.add(transaction.getAmount());
                    case USD -> totalDepositUSD = totalDepositUSD.add(transaction.getAmount());
                    case EUR -> totalDepositEUR = totalDepositEUR.add(transaction.getAmount());
                }
            } else if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                switch (transaction.getCurrency()){
                    case AMD -> totalWithdrawAMD = totalWithdrawAMD.add(transaction.getAmount());
                    case USD -> totalWithdrawUSD = totalWithdrawUSD.add(transaction.getAmount());
                    case EUR -> totalWithdrawEUR = totalWithdrawEUR.add(transaction.getAmount());
                }
            }
            else{
                switch (transaction.getCurrency()){
                    case AMD -> totalTransferAMD = totalTransferAMD.add(transaction.getAmount());
                    case USD -> totalTransferUSD = totalTransferUSD.add(transaction.getAmount());
                    case EUR -> totalTransferEUR = totalTransferEUR.add(transaction.getAmount());
                }
            }
            transactionCounts += 1;
        }

        return new AnalyticsResponse(
                totalDepositAMD, totalDepositUSD, totalDepositEUR,
                totalWithdrawAMD, totalWithdrawUSD, totalWithdrawEUR,
                totalTransferAMD, totalTransferUSD, totalTransferEUR,
                transactionCounts
        );

    }
}
