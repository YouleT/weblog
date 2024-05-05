package com.quanxiaoha.weblog.admin.service;

import com.quanxiaoha.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.quanxiaoha.weblog.common.utils.Response;

/**
 * @author youle
 */
public interface AdminBlogSettingsService {
    /**
     * 更新博客设置信息
     *
     * @param updateBlogSettingsReqVO
     * @return
     */
    Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO);

    /**
     * 获取博客设置详情
     *
     * @return
     */
    Response findDetail();
}
