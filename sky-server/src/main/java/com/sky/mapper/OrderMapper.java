package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("insert into orders (number, status, user_id, address_book_id, order_time, checkout_time, pay_method, pay_status, " +
            "amount, remark, phone, address, consignee, estimated_delivery_time, delivery_status, pack_amount, tableware_number, tableware_status) " +
            "values (#{number}, #{status}, #{userId}, #{addressBookId}, #{orderTime}, #{checkoutTime}, #{payMethod}, #{payStatus}, " +
            "#{amount}, #{remark}, #{phone}, #{address}, #{consignee}, #{estimatedDeliveryTime}, #{deliveryStatus}, #{packAmount}, #{tablewareNumber}, #{tablewareStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Orders orders);


    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    @Update({
            "<script>",
            "update orders ",
            "<set>",
            "   <if test='cancelReason != null and cancelReason != &quot;&quot;'> cancel_reason=#{cancelReason}, </if>",
            "   <if test='rejectionReason != null and rejectionReason != &quot;&quot;'> rejection_reason=#{rejectionReason}, </if>",
            "   <if test='cancelTime != null'> cancel_time=#{cancelTime}, </if>",
            "   <if test='payStatus != null'> pay_status=#{payStatus}, </if>",
            "   <if test='payMethod != null'> pay_method=#{payMethod}, </if>",
            "   <if test='checkoutTime != null'> checkout_time=#{checkoutTime}, </if>",
            "   <if test='status != null'> status = #{status}, </if>",
            "   <if test='deliveryTime != null'> delivery_time = #{deliveryTime}, </if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    void update(Orders orders);


    @Select({
            "<script>",
            "select * from orders ",
            "<where>",
            "   <if test='number != null and number != &quot;&quot;'>",
            "       and number like concat('%',#{number},'%')",
            "   </if>",
            "   <if test='phone != null and phone != &quot;&quot;'>",
            "       and phone like concat('%',#{phone},'%')",
            "   </if>",
            "   <if test='userId != null'>",
            "       and user_id = #{userId}",
            "   </if>",
            "   <if test='status != null'>",
            "       and status = #{status}",
            "   </if>",
            "   <if test='beginTime != null'>",
            "       and order_time &gt;= #{beginTime}",
            "   </if>",
            "   <if test='endTime != null'>",
            "       and order_time &lt;= #{endTime}",
            "   </if>",
            "</where>",
            "order by order_time desc",
            "</script>"
    })
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    Orders getByOrderId(Long id);

    @Select("select count(id) from orders where status = #{status} ")
    Integer countStatus(Integer status);

    @Select("select * from orders where status =#{status}  and order_time < #{time} ")
    List<Orders> getStatusAndOrderTiemLT(Integer status, LocalDateTime time);
}