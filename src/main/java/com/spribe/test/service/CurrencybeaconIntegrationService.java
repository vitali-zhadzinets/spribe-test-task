package com.spribe.test.service;

import java.util.Map;

public interface CurrencybeaconIntegrationService {
  Map<String, Double> fetchRates(String currencyName);
}
