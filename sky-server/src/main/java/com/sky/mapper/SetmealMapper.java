package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.*;

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
            "<script>",
            "select",
            "    s.*, c.name categoryName",
            "from",
            "    setmeal s",
            "left join",
            "    category c",
            "on",
            "    s.category_id = c.id",
            "where 1=1",
            "    <if test='name != null'>",
            "        and s.name like concat('%',#{name},'%')",
            "    </if>",
            "    <if test='status != null'>",
            "        and s.status = #{status}",
            "    </if>",
            "    <if test='categoryId != null'>",
            "        and s.category_id = #{categoryId}",
            "    </if>",
            "order by s.create_time desc",
            "</script>"
    })
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);


    @Delete({"<script>" +
            "delete from setmeal where id in " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>"})
    void deleteById(List<Long> ids);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    @Update({"<script>" +
            "UPDATE setmeal"+
            "<set>"+
            "   <if test='name != null'>name = #{name},</if> "+
            "   <if test='categoryId != null'>category_id = #{categoryId},</if>"+
            "   <if test='price != null'>price = #{price},</if>"+
            "   <if test='image != null'>image = #{image},</if>"+
            "   <if test='description != null'>description = #{description},</if>"+
            "   <if test='status != null'>status = #{status},</if>"+
            "</set>"+
            "WHERE id = #{id}" +"</script>"
    })
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
