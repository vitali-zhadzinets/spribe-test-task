package com.spribe.test.schedule;

import com.spribe.test.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateRatesTask {
  private final CurrencyService currencyService;

  @Autowired
  public UpdateRatesTask(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  @Scheduled(cron = "${application.scheduling.cronExpression}")
  public void updateRates() {
    currencyService.getAllCurrencies().forEach(currencyService::updateRatesCache);
  }
}
