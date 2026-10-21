package org.example.bank_system.controller;


import aj.org.objectweb.asm.commons.AnalyzerAdapter;
import jakarta.validation.Valid;
import org.example.bank_system.dto.AnalyticsRequest;
import org.example.bank_system.dto.AnalyticsResponse;
import org.example.bank_system.service.AnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public AnalyticsResponse transactionAnalytics(@PathVariable String accountNumber,
                                                  @Valid @RequestBody AnalyticsRequest request){
        return analyticsService.getAnalitics(accountNumber, request);
    }
}
