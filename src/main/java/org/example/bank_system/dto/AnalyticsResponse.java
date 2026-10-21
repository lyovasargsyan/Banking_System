package org.example.bank_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AnalyticsResponse {
    private BigDecimal totalDepositAMD;
    private BigDecimal totalDepositUSD;
    private BigDecimal totalDepositEUR;

    private BigDecimal totalWithdrawAMD;
    private BigDecimal totalWithdrawUSD;
    private BigDecimal totalWithdrawEUR;

    private BigDecimal totalTransferAMD;
    private BigDecimal totalTransferUSD;
    private BigDecimal totalTransferEUR;

    private int transactionCounts;

    public AnalyticsResponse(BigDecimal totalDepositAMD,
                             BigDecimal totalDepositUSD,
                             BigDecimal totalDepositEUR,
                             BigDecimal totalWithdrawAMD,
                             BigDecimal totalWithdrawUSD,
                             BigDecimal totalWithdrawEUR,
                             BigDecimal totalTransferAMD,
                             BigDecimal totalTransferUSD,
                             BigDecimal totalTransferEUR,
                             int transactionCounts) {
        this.totalDepositAMD = totalDepositAMD;
        this.totalDepositUSD = totalDepositUSD;
        this.totalDepositEUR = totalDepositEUR;
        this.totalWithdrawAMD = totalWithdrawAMD;
        this.totalWithdrawUSD = totalWithdrawUSD;
        this.totalWithdrawEUR = totalWithdrawEUR;
        this.totalTransferAMD = totalTransferAMD;
        this.totalTransferUSD = totalTransferUSD;
        this.totalTransferEUR = totalTransferEUR;
        this.transactionCounts = transactionCounts;
    }

}
