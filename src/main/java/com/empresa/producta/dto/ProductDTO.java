package com.empresa.producta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

  @NotEmpty(message = "name cannot be empty")
  private String name;

  @NotNull(message = "price cannot be null")
  @DecimalMin(value = "0.0")
  private double price;
}
