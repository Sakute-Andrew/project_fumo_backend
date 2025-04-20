package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.service.dto.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/orders")
public class OrdersController {

    @PostMapping("/create")
    public ResponseEntity<OrderDto> create(OrderDto orderDto) {
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/edit")
    public ResponseEntity<OrderDto> edit(OrderDto orderDto) {
        return ResponseEntity.ok(orderDto);
    }

    @DeleteMapping("/cancel-order/{id}")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable UUID id) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderOfProperty(@PathVariable UUID id) {
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<OrderDto> getOrders() {
        return null;
    }
}
