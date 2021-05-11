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
        try {
            String transactionId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
            String orderJsonString = new String((byte[]) msg.getPayload());
            Order order = new ObjectMapper().readValue(orderJsonString, Order.class);
            this.orderService.createOrder(order, transactionId);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("OrderTransactionListener checkLocalTransaction....");
        String transactionId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        if (transactionId != null && this.transactionLogRepository.existsById(transactionId)) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}
