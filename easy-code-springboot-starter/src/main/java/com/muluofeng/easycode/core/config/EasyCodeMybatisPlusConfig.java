package com.muluofeng.easycode.core.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiexingxing
 * @Created by 2019-12-10 15:44.
 */
@Configuration
@MapperScan(basePackages = "com.muluofeng.easycode.core.dao")
public class EasyCodeMybatisPlusConfig {

    /**
     * 分页插件
     */
    @ConditionalOnMissingBean(PaginationInnerInterceptor.class)
    public PaginationInnerInterceptor paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }

}