package com.pig4cloud.pig.common.security.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.annotation.*;

/**
 *拓展框架注解,有oauth2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableFeignClients
@EnableWebMvc
@EnableTransactionManagement
@EnableAutoConfiguration
public @interface EnableSpringBootAntCloudExcludeResou {

    /**
     * 需要排除的配置
     * @return
     */
    @AliasFor(value = "exclude",annotation = EnableAutoConfiguration.class)
    Class<?>[] value() default {};

    /**
     * 需要排除的配置的Class name
     * @return
     */
    @AliasFor(annotation =EnableAutoConfiguration.class )
    String[] excludeName() default {};

    /**
     * FeignClient 扫描路径
     * @return
     */
    @AliasFor(annotation = EnableFeignClients.class)
    String[] basePackages() default {"com.pig4cloud.pig"};

    /**
     * FeignClient basePackageClasses
     * @return
     */
    @AliasFor(annotation = EnableFeignClients.class)
    Class<?>[] basePackageClasses() default {};

}
