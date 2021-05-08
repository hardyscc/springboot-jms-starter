package com.example.sjs.controller;

import com.example.sjs.entity.Order;
import com.example.sjs.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(path = "/order")
    public void createOrder(@RequestBody Order order) {
        log.info("Create Order {}", order);

        this.orderService.createOrder(order);
    }
}
