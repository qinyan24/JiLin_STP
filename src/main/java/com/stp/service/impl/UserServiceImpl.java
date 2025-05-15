package com.stp.service.impl;

import com.stp.dao.UserDao;
import com.stp.domain.User;
import com.stp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    // 修改为根据用户名、手机号和密码登录
    @Override
    public User login(String username, String phone, String password) {
        return userDao.login(username, phone, password);
    }

    @Override
    public boolean register(User user) {
        // 检查手机号是否已存在
        if (userDao.existsByPhone(user.getPhone())) {
            System.out.println("手机号已存在");
            return false; // 如果手机号已存在，返回 false
        }
        return userDao.register(user) > 0; // 返回注册是否成功
    }
    @Override
    public User getUserById(int id){
        return userDao.findUserById(id);
    }

    @Override
    public void updateUser(User user){
        try {
            userDao.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 实现更新头像方法
    @Override
    public void updateAvatar(User user) {
        try {
            userDao.updateAvatar(user);  // 调用 UserDao 中的 updateAvatar 方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    // 检查旧密码是否正确
//    public boolean checkOldPassword(int id, String oldPassword) {
//        String storedPassword = userDao.getPasswordById(id);
//        return oldPassword.equals(storedPassword);
//    }

    // 更新密码
    public int updatePassword(User user) {
        return userDao.updatePassword(user);
    }

    @Override
    public User getPasswordById(int id) {
        return userDao.getPasswordById(id);
    }
}
