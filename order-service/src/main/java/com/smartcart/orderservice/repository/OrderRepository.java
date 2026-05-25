package com.smartcart.orderservice.repository;

import com.smartcart.orderservice.entity.CustomerOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUserId(Long userId);
}
