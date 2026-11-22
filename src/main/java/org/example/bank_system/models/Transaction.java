package org.example.bank_system.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="transactions", indexes = {
        @Index(name = "transactoins_sender_timestamp", columnList = "senderAccountNumber, timestamp"),
        @Index(name = "transactoins_receiver_timestamp", columnList = "receiverAccountNumber, timestamp")
})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    private String senderAccountNumber;

    @Setter
    private String receiverAccountNumber;

    @Setter
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Setter
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @PrePersist
    public void setTimestampBeforeInsert() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    public Transaction() {
    }
}