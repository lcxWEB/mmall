package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.vo.ProductDetailVO;

/**
 * @Author: lcx
 * @Date: 2018/12/1 14:44
 * @Description:
 */

public interface IProductService {

    /**
     * 新增或更新产品
     * @param product
     * @return
     */
    ServerResponse addOrUpdateProduct(Product product);

    /**
     * 设置商品销售状态
     * @param productId
     * @param status
     * @return
     */
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVO> manageProductDetail(Integer productId);

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    /**
     * 商品查询
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> productSearch(String searchText, Integer pageNum, Integer pageSize);


}
