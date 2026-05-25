package com.smartcart.orderservice.service;

import com.smartcart.orderservice.client.ProductClient;
import com.smartcart.orderservice.dto.OrderRequest;
import com.smartcart.orderservice.dto.OrderResponse;
import com.smartcart.orderservice.dto.ProductResponse;
import com.smartcart.orderservice.dto.StockUpdateRequest;
import com.smartcart.orderservice.entity.CustomerOrder;
import com.smartcart.orderservice.exception.OrderNotFoundException;
import com.smartcart.orderservice.exception.ProductServiceUnavailableException;
import com.smartcart.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderService(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "placeOrderFallback")
    @Retry(name = "productService")
    public OrderResponse placeOrder(OrderRequest request) {
        ProductResponse product = productClient.getProduct(request.productId());
        productClient.reduceStock(request.productId(), new StockUpdateRequest(request.quantity()));

        BigDecimal totalAmount = product.price().multiply(BigDecimal.valueOf(request.quantity()));
        CustomerOrder order = CustomerOrder.builder()
                .userId(request.userId())
                .productId(request.productId())
                .quantity(request.quantity())
                .totalAmount(totalAmount)
                .build();

        CustomerOrder saved = orderRepository.save(order);
        log.info("Placed order {} for user {}", saved.getId(), saved.getUserId());
        return toResponse(saved);
    }

    public OrderResponse placeOrderFallback(OrderRequest request, Throwable throwable) {
        log.warn("Product service unavailable while placing order for product {}", request.productId(), throwable);
        throw new ProductServiceUnavailableException("Product service is currently unavailable. Please try again later.");
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .sorted(Comparator.comparing(CustomerOrder::getId).reversed())
                .map(this::toResponse)
                .toList();
    }

    public List<OrderResponse> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(CustomerOrder::getId).reversed())
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse findById(Long id) {
        return orderRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private OrderResponse toResponse(CustomerOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalAmount());
    }
}
