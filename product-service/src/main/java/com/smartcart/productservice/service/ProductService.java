package com.smartcart.productservice.service;

import com.smartcart.productservice.dto.ProductRequest;
import com.smartcart.productservice.dto.ProductResponse;
import com.smartcart.productservice.entity.Product;
import com.smartcart.productservice.exception.InsufficientStockException;
import com.smartcart.productservice.exception.ProductNotFoundException;
import com.smartcart.productservice.repository.ProductRepository;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .quantity(request.quantity())
                .build();
        Product saved = productRepository.save(product);
        log.info("Created product {}", saved.getId());
        return toResponse(saved);
    }

    public List<ProductResponse> findAll(String keyword, String sortBy) {
        Comparator<Product> comparator = "price".equalsIgnoreCase(sortBy)
                ? Comparator.comparing(Product::getPrice)
                : Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);

        return productRepository.findAll().stream()
                .filter(product -> keyword == null || product.getName().toLowerCase().contains(keyword.toLowerCase()))
                .sorted(comparator)
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse findById(Long id) {
        return productRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        Product saved = productRepository.save(product);
        log.info("Updated product {}", id);
        return toResponse(saved);
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
        log.info("Deleted product {}", id);
    }

    @Transactional
    public ProductResponse reduceStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Requested quantity exceeds available stock");
        }
        product.setQuantity(product.getQuantity() - quantity);
        log.info("Reduced product {} stock by {}", id, quantity);
        return toResponse(product);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity());
    }
}
