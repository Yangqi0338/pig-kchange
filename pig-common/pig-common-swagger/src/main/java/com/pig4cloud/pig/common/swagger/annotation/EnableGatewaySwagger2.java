package com.pig4cloud.pig.common.swagger.annotation;

import com.pig4cloud.pig.common.swagger.config.SwaggerProvider;
import com.pig4cloud.pig.common.swagger.handler.SwaggerAPIHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ SwaggerProvider.class, SwaggerAPIHandler.class})
public @interface EnableGatewaySwagger2 {
}
