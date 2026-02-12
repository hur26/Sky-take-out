package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.enumeration.OperationType;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 插入数据
     * @param category
     */
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Select("<script>"
            + "SELECT * FROM category "
            + "<where>"
            + "  <if test='name != null and name != \"\"'>"
            + "    AND name LIKE CONCAT('%', #{name}, '%')"
            + "  </if>"
            + "  <if test='type != null'>"
            + "    AND type = #{type}"
            + "  </if>"
            + "</where>"
            + "ORDER BY sort ASC, create_time DESC"
            + "</script>")
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据id删除分类
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据id修改分类
     * @param category
     */
    @Update("<script>"
            + "UPDATE category "
            + "<set>"
            + "  <if test='type != null'>"
            + "    type = #{type},"
            + "  </if>"
            + "  <if test='name != null'>"
            + "    name = #{name},"
            + "  </if>"
            + "  <if test='sort != null'>"
            + "    sort = #{sort},"
            + "  </if>"
            + "  <if test='status != null'>"
            + "    status = #{status},"
            + "  </if>"
            + "  <if test='updateTime != null'>"
            + "    update_time = #{updateTime},"
            + "  </if>"
            + "  <if test='updateUser != null'>"
            + "    update_user = #{updateUser}"
            + "  </if>"
            + "</set>"
            + "WHERE id = #{id}"
            + "</script>")
    void update(Category category);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Select("<script>"
            + "SELECT * FROM category "
            + "WHERE status = 1 "
            + "<if test='type != null'>"
            + "  AND type = #{type}"
            + "</if>"
            + "ORDER BY sort ASC, create_time DESC"
            + "</script>")
    List<Category> list(Integer type);
}
