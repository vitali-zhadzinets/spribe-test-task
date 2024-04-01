package com.spribe.test.service;


import com.spribe.test.model.Currency;
import com.spribe.test.schedule.UpdateRatesTask;
import com.spribe.test.service.impl.CurrencyServiceImpl;
import com.spribe.test.service.impl.CurrencybeaconIntegrationServiceImpl;
import com.spribe.test.util.CustomCache;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateRatesTaskTests {
  private static final String TEST_CURRENCY_USD = "USD";
  private static final String TEST_CURRENCY_EUR = "EUR";
  @InjectMocks UpdateRatesTask updateRatesTask;
  @Mock CurrencyServiceImpl currencyService;
  @Mock CustomCache customCache;
  @Mock CurrencybeaconIntegrationServiceImpl currencybeaconIntegrationService;

  @Test
  public void updateRatesTest() {
    Currency currency = Currency.builder().name(TEST_CURRENCY_USD).build();
    when(currencyService.getAllCurrencies()).thenReturn(Collections.singletonList(currency));

    updateRatesTask.updateRates();

    verify(currencyService, times(1)).getAllCurrencies();
    verify(currencyService, times(1)).updateRatesCache(currency);
  }

  @Test
  public void updateRatesTest_MultipleCurrencies() {
    Currency currencyUsd = Currency.builder().name(TEST_CURRENCY_USD).build();
    Currency currencyEur = Currency.builder().name(TEST_CURRENCY_EUR).build();
    when(currencyService.getAllCurrencies()).thenReturn(List.of(currencyUsd, currencyEur));

    updateRatesTask.updateRates();

    verify(currencyService, times(1)).getAllCurrencies();
    verify(currencyService, times(1)).updateRatesCache(currencyUsd);
    verify(currencyService, times(1)).updateRatesCache(currencyEur);
  }
}
