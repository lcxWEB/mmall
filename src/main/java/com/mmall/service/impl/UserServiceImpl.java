package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author: lcx
 * @Date: 2018/11/26 10:50
 * @Description:
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }

        //todo: 密码登录MD5
        String md5Pswd = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Pswd);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户名已存在");
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMsg("email已存在");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("注册失败");
        }
        return ServerResponse.createBySuccessMsg("注册成功");
    }

    /**
     * 检查用户名或邮箱
     *
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        int resultCount;
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("email已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMsg("参数错误");
        }

        return ServerResponse.createBySuccessMsg("校验成功");
    }

    /**
     * 获取忘记密码提示问题
     *
     * @param username
     * @return
     */
    @Override
    public ServerResponse<String> selectQuestion(String username) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        String question = userMapper.selectQuestion(username);
        if (StringUtils.isNoneBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMsg("找回密码的问题是空的");
    }

    /**
     * 校验密码提示问题的答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMsg("答案错误");
    }

    /**
     * 忘记密码重置密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServerResponse<String> forgetResetPswd(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMsg("参数错误，未传token");
        }
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMsg("token无效或已过期");
        }
        if (StringUtils.equals(token, forgetToken)) {
            String md5Pswd = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowcount = userMapper.updatePswd(username, md5Pswd);
            if (rowcount > 0) {
                return ServerResponse.createBySuccessMsg("密码修改成功");
            }
        } else {
            return ServerResponse.createByErrorMsg("token错误，请重新获取");
        }
        return ServerResponse.createByErrorMsg("密码修改失败");
    }


    /**
     * 登录状态下重置密码
     *
     * @param password
     * @param passwordNew
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> resetPswd(String password, String passwordNew, User user) {
        int resultCount = userMapper.checkPswd(MD5Util.MD5EncodeUtf8(password), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int rowcount = userMapper.updateByPrimaryKeySelective(user);
        if (rowcount > 0) {
            return ServerResponse.createBySuccessMsg("密码修改成功");
        }
        return ServerResponse.createByErrorMsg("密码修改失败");
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> updateUserInfo(User user) {
        int resultCount = userMapper.checkEmailById(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMsg("email已存在，请更换email");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int rowcount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (rowcount > 0) {
            return ServerResponse.createBySuccess("用户信息修改成功", updateUser);
        }
        return ServerResponse.createByErrorMsg("用户信息修改失败");
    }

    /**
     * 获取用户详细信息
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<User> getUserInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMsg("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user !=null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }


}
