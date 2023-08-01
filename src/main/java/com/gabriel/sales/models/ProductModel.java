package com.gabriel.sales.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity()
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID idProduct;
  private String name;
  private BigDecimal value;
}
