package com.pig4cloud.pig.gateway.config;

import com.pig4cloud.pig.common.swagger.handler.SwaggerAPIHandler;
import com.pig4cloud.pig.gateway.handler.HystrixFallbackHandler;
import com.pig4cloud.pig.gateway.handler.ImageCodeHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static com.pig4cloud.pig.common.swagger.handler.SwaggerAPIHandler.*;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * @author lengleng
 * @date 2019/2/1
 * 路由配置信息
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {
    private final HystrixFallbackHandler hystrixFallbackHandler;
    private final ImageCodeHandler imageCodeHandler;

    private final SwaggerAPIHandler swaggerAPIHandler;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(
                path("/fallback")
                        .and(accept(TEXT_PLAIN)), hystrixFallbackHandler)
                .andRoute(GET("/code")
                        .and(accept(TEXT_PLAIN)), imageCodeHandler)
                // swagger 路由配置
                .andRoute(GET(SWAGGER_RESOURCES).and(accept(ALL)), swaggerAPIHandler)
                .andRoute(GET(SWAGGER_UI).and(accept(ALL)), swaggerAPIHandler)
                .andRoute(GET(SWAGGER_SECURITY).and(accept(ALL)), swaggerAPIHandler);

    }

}
