package com.nimbleways.springboilerplate.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.handlers.ProductTypeHandler;

@Service
public class ProductService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private List<ProductTypeHandler> handlers;

    @Transactional
    public Long processOrder(Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow();
        order.getItems().forEach(this::processProduct);
        return order.getId();
    }

    private void processProduct(Product product) {
        handlers.stream()
                .filter(h -> h.getSupportedType() == product.getType())
                .findFirst()
                .ifPresent(h -> h.handle(product));
    }
}
