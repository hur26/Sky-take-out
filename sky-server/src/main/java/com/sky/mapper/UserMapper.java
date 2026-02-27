package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openId}")
    User getByOpenId(String openId) ;

    @Insert("insert into user (openid,name,phone,sex,id_number,avatar,create_time) values (#{openid},#{name},#{phone},#{sex},#{idNumber},#{avatar},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Select(("select * from user where id = #{id}"))
    User getById(Long userId);

    @Select({
        "<script>",
                "select count(id) from user",
                "<where>",
                "  <if test='begin != null'>",
                "    and create_time &gt; #{begin}",
                "  </if>",
                "  <if test='end != null'>",
                "    and create_time &lt; #{end}",
                "  </if>",
                "</where>",
                "</script>"
    })
    Integer countByMap(Map map);
}
