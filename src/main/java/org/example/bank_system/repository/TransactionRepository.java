package org.example.bank_system.repository;

import org.example.bank_system.models.Transaction;
import org.example.bank_system.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAccountNumberOrderByTimestampDesc(String senderAccountNumber, String receiverAccountNumber);

    List<Transaction> getTransactionsBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc(String senderAccountNumber, String receiverAccountNumber);

    boolean countByTransactionType(TransactionType transactionType);

}
