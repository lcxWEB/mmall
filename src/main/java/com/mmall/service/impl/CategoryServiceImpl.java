package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author: lcx
 * @Date: 2018/11/29 13:13
 * @Description:
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 增加类目
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("添加类目的参数错误");
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMsg("类目添加成功");
        }
        return ServerResponse.createByErrorMsg("类目添加失败");
    }


    /**
     * 修改类目名称
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse setCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMsg("更新类目名称的参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMsg("类目名称修改成功");
        }
        return ServerResponse.createByErrorMsg("类目名称修改失败");
    }

    /**
     * 获取子类目
     *
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Category>> getChild(Integer categoryId) {
        if (categoryId == null) {
            return ServerResponse.createByErrorMsg("获取类目的参数错误");
        }
        List<Category> categoryList = categoryMapper.getChild(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("当前分类的子分类是空的");
        }
        return ServerResponse.createBySuccess(categoryList);
    }


    /**
     * 递归获取所有子节点
     *
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse getDeepChild(Integer categoryId) {
        if (categoryId == null) {
            return ServerResponse.createByErrorMsg("获取类目的参数错误");
        }
        Set<Category> set = Sets.newHashSet();
        getChildCategory(set, categoryId);
        List<Integer> list = Lists.newArrayList();
        for (Category item : set) {
            list.add(item.getId());
        }
        return ServerResponse.createBySuccess(list);
    }

    private Set<Category> getChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.getChild(categoryId);
        // 查找子节点,递归算法一定要有一个退出的条件
        for (Category item : categoryList) {
            getChildCategory(categorySet, item.getId());
        }
        return categorySet;
    }
}
