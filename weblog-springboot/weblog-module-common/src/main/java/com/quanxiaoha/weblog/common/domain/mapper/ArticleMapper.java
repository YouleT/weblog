package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author youle
 */
public interface ArticleMapper extends BaseMapper<ArticleDO> {
    /**
     * 分页查询
     *
     * @param current   当前页码
     * @param size      每页展示的数据量
     * @param title     文章标题
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    default Page<ArticleDO> selectPageList(Long current, Long size, String title, LocalDate startDate, LocalDate endDate) {
        // 分页对象(查询第几页、每页多少数据)
        Page<ArticleDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<ArticleDO> wrapper = Wrappers.<ArticleDO>lambdaQuery()
                // like 模块查询
                .like(StringUtils.isNotBlank(title), ArticleDO::getTitle, title.trim())
                // 大于等于 startDate
                .ge(Objects.nonNull(startDate), ArticleDO::getCreateTime, startDate)
                // 小于等于 endDate
                .le(Objects.nonNull(endDate), ArticleDO::getCreateTime, endDate)
                // 按创建时间倒叙
                .orderByDesc(ArticleDO::getCreateTime);

        return selectPage(page, wrapper);
    }
}