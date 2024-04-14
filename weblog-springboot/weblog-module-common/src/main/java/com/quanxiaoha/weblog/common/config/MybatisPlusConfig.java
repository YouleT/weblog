package com.quanxiaoha.weblog.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author youle
 */
@Configuration
@MapperScan("com.quanxiaoha.weblog.common.domain.mapper")
public class MybatisPlusConfig {
}