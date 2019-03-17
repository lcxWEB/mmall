package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.pojo.vo.ProductDetailVO;
import com.mmall.pojo.vo.ProductListVO;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lcx
 * @Date: 2018/12/1 14:45
 * @Description:
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增或更新产品
     * @param product
     * @return
     */
    @Override
    public ServerResponse<String> addOrUpdateProduct(Product product) {
        if (product == null) {
            return ServerResponse.createByErrorMsg("商品不能为空");
        }
        if (product.getId() != null) {
            int rowCount = productMapper.updateByPrimaryKey(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMsg("更新产品成功");
            } else {
                return ServerResponse.createBySuccessMsg("更新产品失败");
            }
        }
        int rowCount = productMapper.insert(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMsg("新增产品成功");
        } else {
            return ServerResponse.createBySuccessMsg("新增产品失败");
        }
    }

    /**
     * 更新产品状态
     * @param productId
     * @param status
     * @return
     */
    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId ==null || status == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMsg("设置商品状态成功");
        } else {
            return ServerResponse.createByErrorMsg("设置商品状态失败");
        }
    }

    /**
     * 获取商品详情
     *
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductDetailVO> manageProductDetail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("商品不存在");
        }
        ProductDetailVO productDetailVO = assembleDetailVO(product);
        return ServerResponse.createBySuccess(productDetailVO);
    }

    private ProductDetailVO assembleDetailVO(Product product) {
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setName(product.getName());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        productDetailVO.setStock(product.getStock());

        productDetailVO.setImgHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.mall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVO.setParentCategoryId(0);
        } else {
            productDetailVO.setParentCategoryId(category.getParentId());
        }

        return productDetailVO;
    }


    @Override
    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
        //startpage
        //sql查询逻辑
        //pagehelper收尾
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.listProduct(null);
        List<ProductListVO> productListVOList = new ArrayList<>();
        if (productList != null && !productList.isEmpty()) {
            for (Product product:productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductListVO assembleProductListVO(Product product) {
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setName(product.getName());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setStatus(product.getStatus());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setPrice(product.getPrice());
        productListVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.mall.com/"));
        return productListVO;
    }

    /**
     * 商品查询
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> productSearch(String searchText, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.listProduct(searchText);
        List<ProductListVO> productListVOList = new ArrayList<>();
        if (productList != null && !productList.isEmpty()) {
            for (Product product:productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
