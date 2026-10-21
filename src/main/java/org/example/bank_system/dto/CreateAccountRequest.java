package org.example.bank_system.dto;


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
public class CreateAccountRequest {
    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotBlank(message = "PIN is required")
    @Pattern(
            regexp = "\\d{4}",
            message = "PIN must contain exactly 4 digits"
    )
    private String pinCode;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(
            value = "0.00",
            message = "Initial balance cannot be negative"
    )
    private BigDecimal initialBalance;

    @NotNull(message = "Currency is required")
    private Currency currency;

}
