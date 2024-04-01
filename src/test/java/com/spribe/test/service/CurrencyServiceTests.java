package com.spribe.test.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spribe.test.domain.CurrencyEntity;
import com.spribe.test.domain.repository.CurrencyRepository;
import com.spribe.test.mapper.CurrencyMapper;
import com.spribe.test.model.Currency;
import com.spribe.test.service.impl.CurrencyServiceImpl;
import com.spribe.test.service.impl.CurrencybeaconIntegrationServiceImpl;
import com.spribe.test.util.CustomCache;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTests {
  private static final String TEST_CURRENCY = "USD";
  private static final String TEST_RATE_CURRENCY = "EUR";
  private static final Double TEST_RATE = 1.1d;
  @InjectMocks CurrencyServiceImpl currencyService;
  @Mock CurrencyRepository currencyRepository;
  @Mock CurrencyMapper currencyMapper;
  @Mock CurrencybeaconIntegrationServiceImpl currencybeaconIntegrationService;
  @Mock CustomCache customCache;

  @Test
  public void createCurrencyTest() {
    Currency request = Currency.builder().name(TEST_CURRENCY).build();
    CurrencyEntity currencyEntity = CurrencyEntity.builder().name(TEST_CURRENCY).build();
    Currency savedCurrency = Currency.builder().name(TEST_CURRENCY).build();
    Map<String, Double> rates = Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE);
    when(currencyMapper.currencyToCurrencyEntity(request)).thenReturn(currencyEntity);
    when(currencyRepository.save(any())).thenReturn(currencyEntity);
    when(currencyMapper.currencyEntityToCurrency(currencyEntity)).thenReturn(savedCurrency);
    when(currencybeaconIntegrationService.fetchRates(savedCurrency.getName())).thenReturn(rates);

    Currency result = currencyService.create(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getName(), request.getName());
    verify(currencyMapper, times(1)).currencyToCurrencyEntity(request);
    verify(currencyRepository, times(1)).save(any());
    verify(currencyMapper, times(1)).currencyEntityToCurrency(currencyEntity);
    verify(currencybeaconIntegrationService, times(1)).fetchRates(savedCurrency.getName());
    verify(customCache, times(1)).put(Currency.builder().name(TEST_CURRENCY).rates(rates).build());
  }

  @Test
  public void getAllCurrenciesTest_Pagination() {
    Pageable request = Pageable.unpaged();
    Page<CurrencyEntity> currencyEntityPage =
        new PageImpl<>(
            Collections.singletonList(CurrencyEntity.builder().name(TEST_CURRENCY).build()));
    when(currencyRepository.findAll(request)).thenReturn(currencyEntityPage);
    when(currencyMapper.currencyEntityPageToCurrencyPage(currencyEntityPage))
        .thenReturn(
            new PageImpl<>(
                Collections.singletonList(Currency.builder().name(TEST_CURRENCY).build())));

    Page<Currency> result = currencyService.getAllCurrencies(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1L, result.getTotalElements());
    Assertions.assertEquals(TEST_CURRENCY, result.getContent().getFirst().getName());
    verify(currencyRepository, times(1)).findAll(request);
    verify(currencyMapper, times(1)).currencyEntityPageToCurrencyPage(currencyEntityPage);
  }

  @Test
  public void getAllCurrenciesTest() {
    List<CurrencyEntity> currencyEntityList =
        Collections.singletonList(CurrencyEntity.builder().name(TEST_CURRENCY).build());
    when(currencyRepository.findAll()).thenReturn(currencyEntityList);
    when(currencyMapper.currencyEntityListToCurrencyList(currencyEntityList))
        .thenReturn(Collections.singletonList(Currency.builder().name(TEST_CURRENCY).build()));

    List<Currency> result = currencyService.getAllCurrencies();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1L, result.size());
    Assertions.assertEquals(TEST_CURRENCY, result.getFirst().getName());
    verify(currencyRepository, times(1)).findAll();
    verify(currencyMapper, times(1)).currencyEntityListToCurrencyList(currencyEntityList);
  }
}
