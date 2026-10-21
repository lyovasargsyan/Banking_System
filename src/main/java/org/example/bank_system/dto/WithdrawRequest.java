package org.example.bank_system.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.bank_system.models.Currency;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequest {
    @NotBlank(message = "PIN is required")
    @Pattern(
            regexp = "\\d{4}",
            message = "PIN must contain exactly 4 digits."
    )
    private String pinCode;

    @NotNull(message = "Amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Withdraw amount must be greater than zero."
    )
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    @Enumerated(EnumType.STRING)
    private Currency currency;
}
