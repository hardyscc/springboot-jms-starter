package com.example.sjs.service;


import com.example.sjs.entity.Order;
import com.example.sjs.repository.TransactionLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQTransactionListener(rocketMQTemplateBeanName = "orderRocketMQTemplate")
@AllArgsConstructor
public class OrderTransactionListener implements RocketMQLocalTransactionListener {

    private final OrderService orderService;

    private final TransactionLogRepository transactionLogRepository;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("OrderTransactionListener executeLocalTransaction....");
        RocketMQLocalTransactionState state;
        try {
            String transactionId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
            String orderJsonString = new String((byte[]) msg.getPayload());
            Order order = new ObjectMapper().readValue(orderJsonString, Order.class);
            this.orderService.createOrder(order, transactionId);
            state = RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            state = RocketMQLocalTransactionState.ROLLBACK;
        }
        return state;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("OrderTransactionListener checkLocalTransaction....");
        RocketMQLocalTransactionState state;
        String transactionId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        if (transactionId != null && this.transactionLogRepository.existsById(transactionId)) {
            state = RocketMQLocalTransactionState.COMMIT;
        } else {
            state = RocketMQLocalTransactionState.ROLLBACK;
        }
        return state;
    }
}
