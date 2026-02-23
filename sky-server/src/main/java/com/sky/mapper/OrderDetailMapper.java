package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    @Insert({
            "insert into order_detail (name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount) ",
            "values ",
            "<foreach collection='orderDetailList' item='od' separator=','>",
            "(#{od.name}, #{od.image}, #{od.orderId}, #{od.dishId}, #{od.setmealId}, #{od.dishFlavor}, #{od.number}, #{od.amount})",
            "</foreach>"
    })
    void insertBatch(List<OrderDetail> shoppingCartList);
}
