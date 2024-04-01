package com.spribe.test.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.spribe.test.model.Currency;
import com.spribe.test.service.CurrencyService;
import com.spribe.test.util.CustomCache;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {
  private final CurrencyService currencyService;
  private final CustomCache customCache;

  @Autowired
  public CurrencyController(CurrencyService currencyService, CustomCache customCache) {
    this.currencyService = currencyService;
    this.customCache = customCache;
  }

  @PostMapping
  public ResponseEntity<Currency> create(
      @JsonView(Currency.CreateView.class) @RequestBody Currency request) {
    Currency savedCurrency = currencyService.create(request);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{name}")
                .buildAndExpand(savedCurrency.getName())
                .toUri())
        .body(savedCurrency);
  }

  @GetMapping("/{name}")
  public Currency getCurrencyRates(@PathVariable String name) {
    return Currency.builder().name(name).rates(customCache.get(name)).build();
  }

  @GetMapping
  @JsonView(Currency.CreateView.class)
  public List<Currency> getAllCurrencies() {
    return currencyService.getAllCurrencies();
  }
}
