package com.spribe.test.mapper;

import com.spribe.test.domain.CurrencyEntity;
import com.spribe.test.model.Currency;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
  @Mapping(target = "id", ignore = true)
  CurrencyEntity currencyToCurrencyEntity(Currency source);

  Currency currencyEntityToCurrency(CurrencyEntity source);

  default Page<Currency> currencyEntityPageToCurrencyPage(Page<CurrencyEntity> source) {
    return source.map(this::currencyEntityToCurrency);
  }

  List<Currency> currencyEntityListToCurrencyList(List<CurrencyEntity> source);
}
