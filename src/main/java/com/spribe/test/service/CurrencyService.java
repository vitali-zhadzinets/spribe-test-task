package com.spribe.test.service;

import com.spribe.test.model.Currency;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CurrencyService {
  Currency create(Currency currency);

  Page<Currency> getAllCurrencies(Pageable pageable);

  List<Currency> getAllCurrencies();

  Currency updateRatesCache(Currency currency);
}
