package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    @Insert({
            "<script>",
            "insert into dish_flavor (dish_id, name, value) values ",
            "<foreach collection='list' item='df' separator=','>",
            "(#{df.dishId}, #{df.name}, #{df.value})",
            "</foreach>",
            "</script>"
    })
    void insertBatch(List<DishFlavor > flavors);


}
