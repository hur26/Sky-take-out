package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import net.bytebuddy.implementation.bytecode.constant.MethodConstant;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    @Insert("insert into dish (name, category_id, price, image, description, create_time, update_time, create_user, update_user, status) " +
            "values (#{name},#{categoryId},#{price},#{image},#{description},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish) ;


    @Select("<script>" +
            "select d.*, c.name as categoryName " +
            "from dish d left outer join category c on d.category_id = c.id " +
            "<where>" +
            "   <if test='name != null and name != \"\"'> " +
            "       and d.name like concat('%', #{name}, '%') " +
            "   </if>" +
            "   <if test='categoryId != null'> " +
            "       and d.category_id = #{categoryId} " +
            "   </if>" +
            "   <if test='status != null'> " +
            "       and d.status = #{status} " +
            "   </if>" +
            "</where>" +
            "order by d.create_time" +
            "</script>")
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);


    @Select("select setmeal_dish.setmeal_id from setmeal_dish where dish_id = #{id} ")
    List<Long> getSetmealByDishId(long id);

    @Delete("delete from dish where id = #{id}")
    void deleteById(long id);

    @Delete({
            "<script>",
            "DELETE FROM dish WHERE id IN",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void deleteByIds(List<Long> ids);

    @Select("SELECT * from dish where id = #{id}")
    Dish getById(Long id);

    @Update({
            "<script>",
            "UPDATE dish",
            "<set>",
            "   <if test='name != null'>name = #{name},</if>",
            "   <if test='categoryId != null'>category_id = #{categoryId},</if>",
            "   <if test='price != null'>price = #{price},</if>",
            "   <if test='image != null'>image = #{image},</if>",
            "   <if test='description != null'>description = #{description},</if>",
            "   <if test='status != null'>status = #{status},</if>",
            "   <if test='updateTime != null'>update_time = #{updateTime},</if>",
            "   <if test='updateUser != null'>update_user = #{updateUser},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    @Select({
            "<script>",
            "SELECT * FROM dish",
            "<where>",
            "   <if test='name != null'>",
            "       AND name LIKE CONCAT('%', #{name}, '%')",
            "   </if>",
            "   <if test='categoryId != null'>",
            "       AND category_id = #{categoryId}",
            "   </if>",
            "   <if test='status != null'>",
            "       AND status = #{status}",
            "   </if>",
            "</where>",
            "ORDER BY create_time DESC",
            "</script>"
    })
    List<Dish> list(Dish dish);

    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);
}
