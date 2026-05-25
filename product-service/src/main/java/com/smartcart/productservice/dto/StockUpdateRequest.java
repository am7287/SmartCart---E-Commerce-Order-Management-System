package com.smartcart.productservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockUpdateRequest(@NotNull @Min(1) Integer quantity) {
}
