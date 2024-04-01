package com.spribe.test.mapping;

import com.spribe.test.domain.CurrencyEntity;
import com.spribe.test.mapper.CurrencyMapper;
import com.spribe.test.mapper.CurrencyMapperImpl;
import com.spribe.test.model.Currency;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CurrencyMapperTests {
  private static final String TEST_CURRENCY = "USD";
  private static final String TEST_RATE_CURRENCY = "EUR";
  private static final Double TEST_RATE = 1.1d;
  private static final Integer TEST_PAGE_SIZE = 10;
  private static final Long TEST_TOTAL_ELEMENTS = 1L;
  CurrencyMapper currencyMapper = new CurrencyMapperImpl();

  @Test
  public void currencyToCurrencyEntityTest() {
    Currency source =
        Currency.builder()
            .name(TEST_CURRENCY)
            .rates(Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE))
            .build();

    CurrencyEntity target = currencyMapper.currencyToCurrencyEntity(source);

    Assertions.assertNotNull(target);
    Assertions.assertEquals(TEST_CURRENCY, target.getName());
    Assertions.assertEquals(
        Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE), target.getRates());
  }

  @Test
  public void currencyToCurrencyEntityTest_NullMapping() {
    Currency source = null;

    CurrencyEntity target = currencyMapper.currencyToCurrencyEntity(source);

    Assertions.assertNull(target);
  }

  @Test
  public void currencyEntityToCurrencyTest() {
    CurrencyEntity source =
        CurrencyEntity.builder()
            .name(TEST_CURRENCY)
            .id(1L)
            .rates(Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE))
            .build();

    Currency target = currencyMapper.currencyEntityToCurrency(source);

    Assertions.assertNotNull(target);
    Assertions.assertEquals(TEST_CURRENCY, target.getName());
    Assertions.assertEquals(
        Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE), target.getRates());
  }

  @Test
  public void currencyEntityToCurrencyTest_NullMapping() {
    CurrencyEntity source = null;

    Currency target = currencyMapper.currencyEntityToCurrency(source);

    Assertions.assertNull(target);
  }

  @Test
  public void currencyEntityPageToCurrencyPageTest() {
    Page<CurrencyEntity> source =
        new PageImpl<>(
            Collections.singletonList(CurrencyEntity.builder().name(TEST_CURRENCY).build()),
            Pageable.ofSize(TEST_PAGE_SIZE),
            TEST_TOTAL_ELEMENTS);

    Page<Currency> target = currencyMapper.currencyEntityPageToCurrencyPage(source);

    Assertions.assertNotNull(target);
    Assertions.assertEquals(TEST_TOTAL_ELEMENTS, target.getTotalElements());
    Assertions.assertEquals(TEST_PAGE_SIZE, target.getPageable().getPageSize());
    Assertions.assertNotNull(target.getContent());
    Assertions.assertEquals(TEST_TOTAL_ELEMENTS, target.getContent().size());
    Assertions.assertNotNull(target.getContent().getFirst());
    Assertions.assertEquals(TEST_CURRENCY, target.getContent().getFirst().getName());
  }

  @Test
  public void currencyEntityListToCurrencyList() {
    List<CurrencyEntity> source =
        Collections.singletonList(
            CurrencyEntity.builder()
                .name(TEST_CURRENCY)
                .rates(Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE))
                .build());

    List<Currency> target = currencyMapper.currencyEntityListToCurrencyList(source);

    Assertions.assertNotNull(target);
    Assertions.assertEquals(1, target.size());
    Assertions.assertNotNull(target.getFirst());
    Assertions.assertEquals(TEST_CURRENCY, target.getFirst().getName());
    Assertions.assertEquals(
        Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE), target.getFirst().getRates());
  }

  @Test
  public void currencyEntityListToCurrencyList_NullMapping() {
    List<CurrencyEntity> source = null;

    List<Currency> target = currencyMapper.currencyEntityListToCurrencyList(source);

    Assertions.assertNull(target);
  }
}
