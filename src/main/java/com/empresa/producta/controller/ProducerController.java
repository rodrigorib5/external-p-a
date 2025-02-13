package com.empresa.producta.controller;

import com.empresa.producta.dto.OrderDTO;
import com.empresa.producta.service.amqp.AmqpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/producer")
@RequiredArgsConstructor
@Validated
@Tag(name = "Producer Management", description = "Operations related to message production")
public class ProducerController {

  private final AmqpService service;

  @PostMapping(value = "/send/order")
  @Operation(summary = "Send order to consumer", description = "Sends an order to the consumer with the waiting status")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Accepted",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = Void.class))),
      @ApiResponse(responseCode = "400", description = "Invalid Request",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(example = "{\"error\": \"Invalid order data\"}")))
  })
  public ResponseEntity<Void> sendOrder(@RequestBody @Valid OrderDTO orderDTO) {
    this.service.sendToConsumer(orderDTO.waitingStatus());
    return ResponseEntity.accepted().build();
  }
}
