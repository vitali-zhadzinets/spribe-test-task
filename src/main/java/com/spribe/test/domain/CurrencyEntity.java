package com.spribe.test.domain;

import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity(name = "currency")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_generator")
  @SequenceGenerator(name = "currency_generator", sequenceName = "currency_seq", allocationSize = 1)
  private Long id;

  @NaturalId private String name;

  @ElementCollection
  @CollectionTable(name = "rates", joinColumns = @JoinColumn(name = "currency_id"))
  @MapKeyColumn(name = "currency_name")
  @Column(name = "rate")
  private Map<String, Double> rates;
}
