package com.clh.seckill.mapper;

import com.clh.seckill.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User findById(Long id);

    @Select("select * from user")
    List<User> findAll();


    @Update("update user set password = #{password} where id = #{id}")
    User updatePwd(User toBeUpdate);

    @Insert("insert into user(id,password,salt,register_date) values(#{id},#{password},#{salt},#{registerDate})")
    Integer insert(User user);
}
