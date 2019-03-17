package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author: lcx
 * @Date: 2018/11/29 12:59
 * @Description:
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("addCategory")
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMsg("不是管理员，无权限");
        }
        return iCategoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping("setCategoryName")
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMsg("不是管理员，无权限");
        }
        return iCategoryService.setCategoryName(categoryId, categoryName);
    }

    @RequestMapping("getChild")
    public ServerResponse getChildParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMsg("不是管理员，无权限");
        }
        return iCategoryService.getChild(categoryId);
    }

    @RequestMapping("getDeepChild")
    public ServerResponse getDeepChild(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMsg("不是管理员，无权限");
        }
        return iCategoryService.getDeepChild(categoryId);
    }


}
