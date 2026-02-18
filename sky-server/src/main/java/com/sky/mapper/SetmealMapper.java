package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);



    @Insert("insert into setmeal (category_id, name, price, status, description, image) VALUES (#{categoryId},#{name},#{price},#{status},#{description},#{image})")
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);
}
