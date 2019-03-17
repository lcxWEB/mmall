package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: lcx
 * @Date: 2018/12/3 16:41
 * @Description:
 */
@Service
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fielExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fielExtensionName;
        logger.info("开始上传文件，上传的文件名：{}，上传的路径：{}，新文件名：{}", fileName, path, uploadFileName);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
        //    文件已经上传成功
        // 将targetFile上传到FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
        //  上传完后，删除upload下的文件
            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常" + e);
            return null;
        }
        return targetFile.getName();
    }


}
