package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * @Author: lcx
 * @Date: 2019/1/3 10:46
 * @Description:
 */

public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);
}
