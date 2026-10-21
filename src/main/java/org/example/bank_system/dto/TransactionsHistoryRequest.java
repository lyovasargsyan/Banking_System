package org.example.bank_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionsHistoryRequest {

    @NotBlank(message = "PIN is required")
    @Pattern(
            regexp = "\\d{4}",
            message = "PIN must exactly have 4 digits"
    )
    private String pinCode;
}
