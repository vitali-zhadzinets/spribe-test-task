package com.spribe.test.service;

import com.spribe.test.model.Currency;
import com.spribe.test.util.CustomCache;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomCacheTests {
  private static final String TEST_CURRENCY = "USD";
  private static final String TEST_RATE_CURRENCY = "EUR";
  private static final Double TEST_RATE = 1.1d;
  private static final Double TEST_RATE_NEW_VALUE = 1.2d;
  CustomCache customCache = new CustomCache();

  @Test
  public void customCacheReadWriteTest() {
    customCache.put(
        Currency.builder()
            .name(TEST_CURRENCY)
            .rates(Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE))
            .build());
    Assertions.assertEquals(
        Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE), customCache.get(TEST_CURRENCY));

    customCache.put(
        Currency.builder()
            .name(TEST_CURRENCY)
            .rates(Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE_NEW_VALUE))
            .build());
    Assertions.assertEquals(
        Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE_NEW_VALUE),
        customCache.get(TEST_CURRENCY));
  }
}
