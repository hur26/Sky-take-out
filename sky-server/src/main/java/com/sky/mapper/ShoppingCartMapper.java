package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    @Update("update shopping_cart set number = #{number} where id = #{id}")
     void updateNumberById(ShoppingCart shoppingCarts);

    @Select({
            "<script>",
            "select * from shopping_cart",
            "<where>",
            "<if test=\"userId != null\">",
            "and user_id = #{userId}",
            "</if>",
            "<if test=\"setmealId != null\">",
            "and setmeal_id = #{setmealId}",
            "</if>",
            "<if test=\"dishId != null\">",
            "and dish_id = #{dishId}",
            "</if>",
            "<if test=\"dishFlavor != null\">",
            "and dish_flavor = #{dishFlavor}",
            "</if>",
            "</where>",
            "</script>"
    })
    @ResultType(com.sky.entity.ShoppingCart.class)
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) VALUES (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);
}
