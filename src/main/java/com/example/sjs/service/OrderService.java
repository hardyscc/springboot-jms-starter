package com.example.sjs.service;

import com.example.sjs.entity.Order;
import com.example.sjs.entity.TransactionLog;
import com.example.sjs.repository.OrderRepository;
import com.example.sjs.repository.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;

@Service
public class OrderService {

    @Resource(name = "orderRocketMQTemplate")
    private OrderRocketMQTemplate orderRocketMQTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    public void createOrder(Order order) {
        if (this.orderRepository.existsById(order.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order already exists!");
        }
        this.orderRocketMQTemplate.send(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Order order, String transactionId) {

        Order savedOrder = this.orderRepository.save(order);
        this.transactionLogRepository.save(
                TransactionLog.builder()
                        .id(transactionId)
                        .business(order.getClass().getName())
                        .foreignKey(String.valueOf(savedOrder.getId()))
                        .build()
        );
    }
}
