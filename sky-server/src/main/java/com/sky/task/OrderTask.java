package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 */15 * * * *")
    public void processTimeOutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orders = orderMapper.getStatusAndOrderTiemLT(Orders.PENDING_PAYMENT,time);
        if(orders.size()>0 && orders != null){
            for(Orders orders1 : orders){
                orders1.setStatus(Orders.CANCELLED);
                orders1.setRejectionReason("订单超时自动取消");
                orders1.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders1);
            }
        }
    }


    @Scheduled(cron = "0 0 1 * * *")
    public void processDeliveryOrder(){
        log.info("处理一直派送的订单");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders = orderMapper.getStatusAndOrderTiemLT(Orders.DELIVERY_IN_PROGRESS,time);
        if(orders.size()>0 && orders != null){
            for(Orders orders1 : orders){
                orders1.setStatus(Orders.COMPLETED);
                orders1.setRejectionReason("订单自动完成");
                orders1.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders1);
            }
        }

    }
}
