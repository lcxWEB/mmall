package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @Author: lcx
 * @Date: 2018/11/26 10:47
 * @Description:
 */

public interface IUserService {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 注册
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 检查用户名或邮箱
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 获取忘记密码提示问题
     * @param username
     * @return
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 校验密码提示问题的答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);


    /**
     * 忘记密码重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetResetPswd(String username, String passwordNew, String forgetToken);

    /**
     * 登录状态下重置密码
     * @param password
     * @param passwordNew
     * @param user
     * @return
     */
    ServerResponse<String> resetPswd(String password, String passwordNew, User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    ServerResponse<User> updateUserInfo(User user);

    /**
     * 获取用户详细信息
     * @param userId
     * @return
     */
    ServerResponse<User> getUserInfo(Integer userId);

    /**
     * 检查是否是管理员
     * @param user
     * @return
     */
    ServerResponse checkAdminRole(User user);

}
