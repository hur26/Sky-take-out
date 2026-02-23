package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 条件查询
     * @param addressBook
     * @return
     */
    @Select({
            "<script>",
            "select * from address_book",
            "<where>",
            "   <if test='userId != null'> and user_id = #{userId} </if>",
            "   <if test='phone != null'> and phone = #{phone} </if>",
            "   <if test='isDefault != null'> and is_default = #{isDefault} </if>",
            "</where>",
            "</script>"
    })
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 新增
     * @param addressBook
     */
    @Insert("insert into address_book" +
            "        (user_id, consignee, phone, sex, province_code, province_name, city_code, city_name, district_code," +
            "         district_name, detail, label, is_default)" +
            "        values (#{userId}, #{consignee}, #{phone}, #{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}," +
            "                #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void insert(AddressBook addressBook);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 根据id修改
     * @param addressBook
     */
    @Update({
            "<script>",
            "update address_book",
            "<set>",
            "   <if test='consignee != null'> consignee = #{consignee}, </if>",
            "   <if test='sex != null'> sex = #{sex}, </if>",
            "   <if test='phone != null'> phone = #{phone}, </if>",
            "   <if test='detail != null'> detail = #{detail}, </if>",
            "   <if test='label != null'> label = #{label}, </if>",
            "   <if test='isDefault != null'> is_default = #{isDefault}, </if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    void update(AddressBook addressBook);

    /**
     * 根据 用户id修改 是否默认地址
     * @param addressBook
     */
    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);

}
