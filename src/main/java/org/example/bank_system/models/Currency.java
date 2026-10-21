package org.example.bank_system.models;

import lombok.Getter;

import java.math.BigDecimal;


public enum Currency {
    AMD(1),
    USD(360),
    EUR(400);

    private final int value;

    Currency(int value) {
        this.value = value;
    }

    public BigDecimal getValue(){
        return BigDecimal.valueOf(value);
    }

}
