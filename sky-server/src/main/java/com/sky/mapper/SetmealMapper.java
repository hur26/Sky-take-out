package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select({
            "<script>" +
                    "select * from setmeal " +
                    "<where>" +
                    "<if test='name != null and name != \"\"'>" +
                    "and name like concat('%', #{name}, '%') " +
                    "</if>" +
                    "<if test='categoryId != null'>" +
                    "and categoryId = #{categoryId} " +
                    "</if>" +
                    "</where>" +
                    "order by create_time desc" +
                    "</script>"})
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);


    @Delete({"<script>" +
            "delete from setmeal where id in " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>"})
    void deleteById(List<Integer> ids);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Integer id);
}
