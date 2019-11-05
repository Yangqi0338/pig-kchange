package com.pig4cloud.pig.common.swagger.annotation;

import com.pig4cloud.pig.common.swagger.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lengleng
 * @date 2018/7/21
 * 开启pigx swagger
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class,})
public @interface EnableSwagger2Pro {
}
