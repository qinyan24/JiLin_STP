package com.stp.service;

import com.stp.domain.User;

public interface UserService {
    // 修改为根据用户名和手机号登录
    User login(String username, String phone, String password);

    // 注册方法不需要修改
    boolean register(User user);

    //获取用户id
    User getUserById(int id);

    //更新用户信息
    void updateUser(User user);

    void updateAvatar(User user);

    int updatePassword( User user);

    User getPasswordById(int id);
}
