package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: lcx
 * @Date: 2018/12/3 16:41
 * @Description:
 */

public interface IFileService {

    String upload(MultipartFile file, String path);
}
