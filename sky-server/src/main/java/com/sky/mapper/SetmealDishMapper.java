package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    @Insert({"<script>",
            "insert into setmeal_dish (setmeal_id, dish_id, name, price, copies) values ",
            "<foreach collection='setmealDishes' item='sd' separator=','>",
            "(#{sd.setmealId}, #{sd.dishId}, #{sd.name}, #{sd.price}, #{sd.copies})",
            "</foreach>",
            "</script>"})
    void insert(List<SetmealDish> setmealDishes);

    @Select("select * from dish where id = #{id}")
    public Dish getById(Long id);


    @Delete({"<script>"+"delete from setmeal_dish where id in " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>"})
    void deleteBySetmealId(List<Integer> ids);
}
