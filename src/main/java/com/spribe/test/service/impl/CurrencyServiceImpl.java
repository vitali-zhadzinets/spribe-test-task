package com.spribe.test.service.impl;

import com.spribe.test.domain.repository.CurrencyRepository;
import com.spribe.test.mapper.CurrencyMapper;
import com.spribe.test.model.Currency;
import com.spribe.test.service.CurrencyService;
import com.spribe.test.util.CustomCache;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {
  private final CurrencyRepository currencyRepository;
  private final CurrencyMapper currencyMapper;
  private final CurrencybeaconIntegrationServiceImpl currencybeaconIntegrationService;
  private final CustomCache customCache;

  @Autowired
  public CurrencyServiceImpl(
      CurrencyRepository currencyRepository,
      CurrencyMapper currencyMapper,
      CurrencybeaconIntegrationServiceImpl currencybeaconIntegrationService,
      CustomCache customCache) {
    this.currencyRepository = currencyRepository;
    this.currencyMapper = currencyMapper;
    this.currencybeaconIntegrationService = currencybeaconIntegrationService;
    this.customCache = customCache;
  }

  @Override
  public Currency create(Currency currency) {
    return currencyMapper.currencyEntityToCurrency(
        currencyRepository.save(
            currencyMapper.currencyToCurrencyEntity(updateRatesCache(currency))));
  }

  @Override
  public Page<Currency> getAllCurrencies(Pageable pageable) {
    return currencyMapper.currencyEntityPageToCurrencyPage(currencyRepository.findAll(pageable));
  }

  @Override
  @Transactional
  public List<Currency> getAllCurrencies() {
    return currencyMapper.currencyEntityListToCurrencyList(currencyRepository.findAll());
  }

  @Override
  public Currency updateRatesCache(Currency currency) {
    currency.setRates(currencybeaconIntegrationService.fetchRates(currency.getName()));
    customCache.put(currency);
    return currency;
  }
}
