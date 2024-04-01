package com.spribe.test.integration;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spribe.test.model.Currency;
import com.spribe.test.model.integration.CurrencybeaconResponse;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyIntegrationTests {
  private static final String TEST_RATE_CURRENCY = "EUR";
  private static final Double TEST_RATE = 1.1d;
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private RestTemplate restTemplate;

  @Test
  public void createNewCurrency() throws Exception {
    Mockito.when(restTemplate.getForObject(any(), any()))
        .thenReturn(
            new CurrencybeaconResponse(Collections.singletonMap(TEST_RATE_CURRENCY, TEST_RATE)));

    mockMvc
        .perform(
            post("/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Currency.builder().name("USD").build())))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("USD")))
        .andExpect(jsonPath("$.rates." + TEST_RATE_CURRENCY).exists())
        .andExpect(jsonPath("$.rates." + TEST_RATE_CURRENCY, is(TEST_RATE)));
  }

  @Test
  public void createNewCurrency_CantRetrieveRates() throws Exception {
    mockMvc
        .perform(
            post("/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Currency.builder().name("EUR").build())))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("EUR")))
        .andExpect(jsonPath("$.rates").isEmpty());
  }

  @Test
  public void retrieveListOfCurrencies() throws Exception {
    mockMvc
        .perform(get("/currencies").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("EUR", "USD")));
  }

  @Test
  public void retrieveCurrency() throws Exception {
    mockMvc
        .perform(get("/currencies/USD").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("USD")))
        .andExpect(jsonPath("$.rates." + TEST_RATE_CURRENCY).exists())
        .andExpect(jsonPath("$.rates." + TEST_RATE_CURRENCY, is(TEST_RATE)));
  }

  @Test
  public void retrieveCurrency_NonExistentValue() throws Exception {
    mockMvc
        .perform(get("/currencies/QWE").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("QWE")))
        .andExpect(jsonPath("$.rates", is(nullValue())));
  }
}
