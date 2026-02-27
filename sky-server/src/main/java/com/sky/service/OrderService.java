package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    void paySuccess(String outTradeNo);


    PageResult pageQuery4User(int page, int pageSize, Integer status);

    OrderVO detailQuery(Long id);

    void cancel(Long id)throws Exception;

    void repetition(Long id);

    PageResult searchOrder(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

    void rejectOrders(OrdersRejectionDTO ordersRejectionDTO);

    void cancel(OrdersCancelDTO ordersCancelDTO)throws Exception;

    void delivery(Long id);

    void complete(Long id);

    void reminder(Long id);
}
