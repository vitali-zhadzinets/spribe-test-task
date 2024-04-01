package com.spribe.test.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spribe.test.model.integration.CurrencybeaconResponse;
import com.spribe.test.service.impl.CurrencybeaconIntegrationServiceImpl;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class CurrencybeaconIntegrationServiceTests {
  private static final String TEST_DEFAULT_CURRENCY = "USD";
  private static final String TEST_RATE_CURRENCY = "EUR";
  private static final Double TEST_RATE = 1.1d;
  @InjectMocks CurrencybeaconIntegrationServiceImpl currencybeaconIntegration;
  @Mock RestTemplate restTemplate;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(
        currencybeaconIntegration, "baseUrl", "https://api.currencybeacon.com/v1/");
    ReflectionTestUtils.setField(currencybeaconIntegration, "fetchRatesUri", "latest");
    ReflectionTestUtils.setField(currencybeaconIntegration, "currencyParam", "base");
    ReflectionTestUtils.setField(currencybeaconIntegration, "defaultCurrency", "USD");
    ReflectionTestUtils.setField(currencybeaconIntegration, "apiKeyParam", "api_key");
    ReflectionTestUtils.setField(currencybeaconIntegration, "apiKey", "apiKey");
  }

  @Test
  public void fetchRates() {
    when(restTemplate.getForObject(any(), any()))
        .thenReturn(
            new CurrencybeaconResponse(Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE)));

    Map<String, Double> result = currencybeaconIntegration.fetchRates(TEST_RATE_CURRENCY);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE), result);
    verify(restTemplate, times(1)).getForObject(any(), any());
  }

  @Test
  public void fetchRates_EmptyResult() {
    when(restTemplate.getForObject(any(), any()))
        .thenReturn(null)
        .thenReturn(
            new CurrencybeaconResponse(Collections.singletonMap(TEST_DEFAULT_CURRENCY, TEST_RATE)));

    Map<String, Double> result = currencybeaconIntegration.fetchRates(TEST_RATE_CURRENCY);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(Collections.singletonMap(TEST_DEFAULT_CURRENCY, TEST_RATE), result);
    verify(restTemplate, times(2)).getForObject(any(), any());
  }

  @Test
  public void fetchRates_NullResult() {
    when(restTemplate.getForObject(any(), any())).thenReturn(null).thenReturn(null);

    Map<String, Double> result = currencybeaconIntegration.fetchRates(TEST_RATE_CURRENCY);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(Collections.emptyMap(), result);
    verify(restTemplate, times(2)).getForObject(any(), any());
  }
}
