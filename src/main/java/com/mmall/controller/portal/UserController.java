package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author: lcx
 * @Date: 2018/11/26 10:25
 * @Description: 用户模块
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logOut", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logOut(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @RequestMapping(value = "checkValid", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMsg("用户未登录");
    }

    @RequestMapping(value = "forgetGetQuestion", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    @RequestMapping(value = "forgetCheckAnswer", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forgetResetPswd", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetResetPswd(String username, String passwordNew, String forgetToken){
        return iUserService.forgetResetPswd(username, passwordNew, forgetToken);
    }

    @RequestMapping(value = "resetPswd", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> resetPswd(String password, String passwordNew, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        return iUserService.resetPswd(password, passwordNew, user);
    }

    @RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session, User user){
        User currentuser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentuser == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        user.setId(currentuser.getId());
        ServerResponse<User> response = iUserService.updateUserInfo(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }


    @RequestMapping(value = "getUserInfoDetail", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfoDetail(HttpSession session){
        User currentuser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentuser == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要先登录");
        }
        return iUserService.getUserInfo(currentuser.getId());
    }

}
