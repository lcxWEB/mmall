package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @Author: lcx
 * @Date: 2018/11/29 13:13
 * @Description:
 */

public interface ICategoryService {

    /**
     * 增加类目
     * @param categoryName
     * @param parentId
     * @return
     */
    ServerResponse addCategory(String categoryName, Integer parentId);

    /**
     * 修改类目名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    ServerResponse setCategoryName(Integer categoryId, String categoryName);

    /**
     * 获取子类目
     * @param categoryId
     * @return
     */
    ServerResponse<List<Category>> getChild(Integer categoryId);

    /**
     * 递归获取所有子节点
     * @param categoryId
     * @return
     */
    ServerResponse getDeepChild(Integer categoryId);

}
