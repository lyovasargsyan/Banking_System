package org.example.bank_system.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bank_system.models.Currency;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountResponse {
    private Long id;
    private String ownerName;
    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;

    public AccountResponse(Long id, String ownerName, String accountNumber, BigDecimal balance, Currency currency) {
        this.id = id;
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
    }
}
