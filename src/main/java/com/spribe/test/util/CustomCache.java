package com.spribe.test.util;

import com.spribe.test.model.Currency;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class CustomCache {
  private final ConcurrentHashMap<String, Map<String, Double>> customCache =
      new ConcurrentHashMap<>();

  public void put(Currency currency) {
    customCache.put(currency.getName(), currency.getRates());
  }

  public Map<String, Double> get(String key) {
    return customCache.get(key);
  }
}
