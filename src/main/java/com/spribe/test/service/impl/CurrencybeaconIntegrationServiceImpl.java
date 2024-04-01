package com.spribe.test.service.impl;

import com.spribe.test.model.integration.CurrencybeaconResponse;
import com.spribe.test.service.CurrencybeaconIntegrationService;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CurrencybeaconIntegrationServiceImpl implements CurrencybeaconIntegrationService {
  private final RestTemplate restTemplate;

  @Value("${application.currencybeacon.baseUrl}")
  private String baseUrl;

  @Value("${application.currencybeacon.latestPath}")
  private String fetchRatesUri;

  @Value("${application.currencybeacon.currencyParam}")
  private String currencyParam;

  @Value("${application.currencybeacon.apiKeyParam}")
  private String apiKeyParam;

  @Value("${application.currencybeacon.apiKey}")
  private String apiKey;

  @Value("${application.defaultCurrency}")
  private String defaultCurrency;

  @Autowired
  public CurrencybeaconIntegrationServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Map<String, Double> fetchRates(String currencyName) {
    try {
      CurrencybeaconResponse response =
          restTemplate.getForObject(
              UriComponentsBuilder.fromHttpUrl(baseUrl)
                  .path(fetchRatesUri)
                  .queryParam(currencyParam, currencyName)
                  .queryParam(apiKeyParam, apiKey)
                  .build()
                  .toUri(),
              CurrencybeaconResponse.class);
      return response.rates();
    } catch (Exception exception) {
      if (currencyName.equals(defaultCurrency)) {
        return Collections.emptyMap();
      }
      return fetchRates(defaultCurrency);
    }
  }
}
