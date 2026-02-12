package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee (name, username , password , phone , sex , id_number , create_time , update_time , create_user , update_user , status) " +
            "VALUES " + "(#{name} , #{username} , #{password} , #{phone} , #{sex} , #{idNumber} , #{createTime} , #{updateTime} , #{createUser} , #{updateUser} , #{status})")
    void insert (Employee employee);

    @Select("select * from employee " +
            "where (#{name} is null or #{name} = '' or name like concat('%', #{name}, '%')) " +
            "order by create_time desc")
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) ;

    @Update("<script>"
            + "UPDATE employee "
            + "<set>"
            + "<if test='name != null'>name = #{name},</if>"
            + "<if test='username != null'>username = #{username},</if>"
            + "<if test='password != null'>password = #{password},</if>"
            + "<if test='phone != null'>phone = #{phone},</if>"
            + "<if test='sex != null'>sex = #{sex},</if>"
            + "<if test='idNumber != null'>idNumber = #{idNumber},</if>"
            + "<if test='updateTime != null'>updateTime = #{updateTime},</if>"
            + "<if test='updateUser != null'>updateUser = #{updateUser},</if>"
            + "<if test='status != null'>status = #{status},</if>"
            + "</set>"
            + "WHERE id = #{id}"
            + "</script>")
    void update(Employee employee);
}
