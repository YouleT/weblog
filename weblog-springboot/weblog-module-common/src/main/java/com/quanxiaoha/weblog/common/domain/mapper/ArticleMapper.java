package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticlePublishCountDO;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
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
                .like(StringUtils.isNotBlank(title), ArticleDO::getTitle, title)
                // 大于等于 startDate
                .ge(Objects.nonNull(startDate), ArticleDO::getCreateTime, startDate)
                // 小于等于 endDate
                .le(Objects.nonNull(endDate), ArticleDO::getCreateTime, endDate)
                // 按创建时间倒叙
                .orderByDesc(ArticleDO::getCreateTime);

        return selectPage(page, wrapper);
    }

    /**
     * 根据文章 ID 批量分页查询
     *
     * @param current
     * @param size
     * @param articleIds
     * @return
     */
    default Page<ArticleDO> selectPageListByArticleIds(Long current, Long size, List<Long> articleIds) {
        // 分页对象(查询第几页、每页多少数据)
        Page<ArticleDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<ArticleDO> wrapper = Wrappers.<ArticleDO>lambdaQuery()
                // 批量查询
                .in(ArticleDO::getId, articleIds)
                // 按创建时间倒叙
                .orderByDesc(ArticleDO::getCreateTime);

        return selectPage(page, wrapper);
    }

    /**
     * 查询上一篇文章
     *
     * @param articleId
     * @return
     */
    default ArticleDO selectPreArticle(Long articleId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                // 按文章 ID 升序排列
                .orderByAsc(ArticleDO::getId)
                // 查询比当前文章 ID 大的
                .gt(ArticleDO::getId, articleId)
                // 第一条记录即为上一篇文章
                .last("limit 1"));
    }

    /**
     * 查询下一篇文章
     *
     * @param articleId
     * @return
     */
    default ArticleDO selectNextArticle(Long articleId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                // 按文章 ID 倒序排列
                .orderByDesc(ArticleDO::getId)
                // 查询比当前文章 ID 小的
                .lt(ArticleDO::getId, articleId)
                // 第一条记录即为下一篇文章
                .last("limit 1"));
    }

    /**
     * 阅读量+1
     *
     * @param articleId
     * @return
     */
    default int increaseReadNum(Long articleId) {
        // 执行 SQL : UPDATE t_article SET read_num = read_num + 1 WHERE (id = XX)
        return update(null, Wrappers.<ArticleDO>lambdaUpdate()
                .setSql("read_num = read_num + 1")
                .eq(ArticleDO::getId, articleId));
    }

    /**
     * 查询所有记录的阅读量
     *
     * @return
     */
    default List<ArticleDO> selectAllReadNum() {
        // 设置仅查询 read_num 字段
        return selectList(Wrappers.<ArticleDO>lambdaQuery()
                .select(ArticleDO::getReadNum));
    }

    /**
     * 按日分组，并统计每日发布的文章数量
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Select("SELECT DATE(create_time) AS date, COUNT(*) AS count FROM t_article WHERE create_time >= #{startDate} AND create_time < #{endDate} GROUP BY DATE(create_time)")
    List<ArticlePublishCountDO> selectDateArticlePublishCount(LocalDate startDate, LocalDate endDate);
}
