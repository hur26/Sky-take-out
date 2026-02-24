package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    @Insert({
            "<script>",
            "insert into order_detail (name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount) ",
            "values ",
            "<foreach collection='shoppingCartList' item='od' separator=','>",
            "(#{od.name}, #{od.image}, #{od.orderId}, #{od.dishId}, #{od.setmealId}, #{od.dishFlavor}, #{od.number}, #{od.amount})",
            "</foreach>",
            "</script>"
    })
    void insertBatch(List<OrderDetail> shoppingCartList);

    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long orderId);

    @Delete("delete from order_detail where order_id = #{orderId}")
    void deleteByOdrerId(Long orderId);
}
