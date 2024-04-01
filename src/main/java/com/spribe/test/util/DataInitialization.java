package com.spribe.test.util;

import com.spribe.test.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataInitialization implements ApplicationListener<ContextRefreshedEvent> {
  private final CustomCache customCache;
  private final CurrencyService currencyService;

  @Autowired
  public DataInitialization(CustomCache customCache, CurrencyService currencyService) {
    this.customCache = customCache;
    this.currencyService = currencyService;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    currencyService.getAllCurrencies().forEach(customCache::put);
  }
}
