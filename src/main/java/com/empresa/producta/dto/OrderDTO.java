package com.empresa.producta.dto;

import com.empresa.producta.domain.DomainStatusMessage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Data;

@Data
public class OrderDTO {

  @NotEmpty(message = "Product list cannot be empty")
  private List<@NotNull ProductDTO> products;

  private double totalAmount;

  private String status;

  @NotEmpty(message = "idempotencyKey cannot be empty")
  @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "xIdempotencyKey must be alphanumeric and can include hyphens")
  private String idempotencyKey;

  public OrderDTO waitingStatus() {
    this.setStatus(DomainStatusMessage.WAITING.name());
    return this;
  }
}
