package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.*;

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
            "update orders ",
            "<set>",
            "   <if test='cancelReason != null and cancelReason!=\"\" '> cancel_reason=#{cancelReason}, </if>",
            "   <if test='rejectionReason != null and rejectionReason!=\"\" '> rejection_reason=#{rejectionReason}, </if>",
            "   <if test='cancelTime != null'> cancel_time=#{cancelTime}, </if>",
            "   <if test='payStatus != null'> pay_status=#{payStatus}, </if>",
            "   <if test='payMethod != null'> pay_method=#{payMethod}, </if>",
            "   <if test='checkoutTime != null'> checkout_time=#{checkoutTime}, </if>",
            "   <if test='status != null'> status = #{status}, </if>",
            "   <if test='deliveryTime != null'> delivery_time = #{deliveryTime} </if>",
            "</set>",
            "where id = #{id}"
    })
    void update(Orders orders);



}