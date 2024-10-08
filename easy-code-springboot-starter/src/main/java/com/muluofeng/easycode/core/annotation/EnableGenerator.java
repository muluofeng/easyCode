package com.muluofeng.easycode.core.annotation;

import com.muluofeng.easycode.core.config.GeneratorAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiexingxing
 * @date 2024/8/29 16:30
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(GeneratorAutoConfig.class)
public @interface EnableGenerator {
}
