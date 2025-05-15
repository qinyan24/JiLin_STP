package com.stp.dao;

import com.stp.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {

    // 根据用户名、手机号和密码登录
    @Select("SELECT * FROM users WHERE username = #{username} AND phone = #{phone} AND password = #{password}")
    User login(@Param("username") String username, @Param("phone") String phone, @Param("password") String password);

    // 注册之前检查手机号是否存在
    @Select("SELECT COUNT(*) FROM users WHERE phone = #{phone}")
    boolean existsByPhone(String phone);

    // 注册用户
    @Insert("INSERT INTO users (username, phone, password) VALUES (#{username}, #{phone}, #{password})")
    int register(User user);

    @Select("SELECT username ,phone ,image_url,description FROM users WHERE id = #{id} LIMIT 1")
    @Result(property = "imageUrl",column = "image_url")
    User findUserById(int id);

//    // 插入用户数据时，包含 image_url 和 description 字段
//    @Insert("INSERT INTO users (username, phone, password, image_url, description) " +
//            "VALUES (#{username}, #{phone}, #{password}, #{imageUrl}, #{description})")
//    void insertUser(User user);

    // 更新用户信息时，包含 image_url 和 description 字段
    @Update("UPDATE users SET username = #{username}, phone = #{phone}, " +
            " description = #{description} WHERE id = #{id}")
    void updateUser(User user);

    // 更新用户头像URL
    @Update("UPDATE users SET image_url = #{imageUrl} WHERE id = #{id}")
//    @Result(property = "imageUrl",column = "image_url")
    void updateAvatar(User user);

    @Select("SELECT password FROM users WHERE id = #{id}")
    User getPasswordById(int id);

    @Update("UPDATE users SET password = #{password} WHERE id = #{id}")
    int updatePassword(User user);

}
