package com.mmall.controller.portal;

import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lcx
 * @Date: 2018/12/19 12:45
 * @Description:
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

}
