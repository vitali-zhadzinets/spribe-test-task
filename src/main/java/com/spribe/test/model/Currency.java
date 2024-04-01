package com.spribe.test.model;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Currency {
  @JsonView({CreateView.class, GetAllResponseView.class})
  private String name;

  private Map<String, Double> rates;

  public interface CreateView {}

  public interface GetAllResponseView {}
}
