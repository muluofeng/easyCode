package com.muluofeng.easycode.core.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.muluofeng.easycode.core.utils.RRException;
import jakarta.annotation.Resource;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author xiexingxing
 * @Created by 2020-09-30 11:00.
 */
@ComponentScan(value = "com.muluofeng.easycode.**")
@MapperScan(basePackages = "com.muluofeng.easycode.core.dao")
@Configuration
public class GeneratorAutoConfig {

    @Resource
    private DataSource dataSource;

    @Bean
    org.apache.commons.configuration.Configuration easyCodePropertiesConfiguration() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }


    @Bean
    @ConditionalOnClass(DynamicRoutingDataSource.class)
    public DataSourceHandle dynamicRoutingDataSourceHandle() {
        return new DynamicRoutingDataSourceHandle((DynamicRoutingDataSource) dataSource);
    }

    @Bean
    @ConditionalOnMissingBean(DataSourceHandle.class)
    public DataSourceHandle DefaultDataSourceHandle() {
        return new DefaultDataSourceHandle();
    }

}