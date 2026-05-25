package com.smartcart.orderservice.client;

import com.smartcart.orderservice.dto.ProductResponse;
import com.smartcart.orderservice.dto.StockUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", path = "/api/products")
public interface ProductClient {
    @GetMapping("/{id}")
    ProductResponse getProduct(@PathVariable("id") Long id);

    @PutMapping("/{id}/stock/reduce")
    ProductResponse reduceStock(@PathVariable("id") Long id, @RequestBody StockUpdateRequest request);
}
