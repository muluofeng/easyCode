
package com.muluofeng.easycode.core.config;

import com.muluofeng.easycode.core.utils.RRException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiexingxing
 * @Created by 2020-09-30 11:00.
 */
@ComponentScan(value = "com.muluofeng.easycode.**")
@Configuration
public class GeneratorAutoConfig {


    @Bean
    org.apache.commons.configuration.Configuration easyCodePropertiesConfiguration(){
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }
}