package com.quanxiaoha.weblog.admin.service;

import com.quanxiaoha.weblog.common.utils.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author youle
 */
public interface AdminFileService {
    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    Response uploadFile(MultipartFile file);
}
