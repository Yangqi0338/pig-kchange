package com.pig4cloud.pig.common.swagger.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

/**
 * 网关输出数据构造
 */
public abstract class BaseHandler implements HandlerFunction<ServerResponse> {

    /**
     * swagger api
     */
    public static final String SWAGGER_UI = "/swagger-resources/configuration/ui";
    /**
     * swagger api
     */
    public static final String SWAGGER_RESOURCES = "/swagger-resources";
    /**
     * swagger api
     */
    public static final String SWAGGER_SECURITY = "/swagger-resources/configuration/security";

    /**
     * 构造 Mono
     *
     * @param t     需输出数据
     * @param type  输出的文本类型
     * @param clazz 输出的请求状态
     * @return Mono
     */
    protected <T> Mono<ServerResponse> createMono(MediaType type, HttpStatus clazz, T t) {
        // 设置请求状态
        return status(clazz.value())
                // 文本类型
                .contentType(type).body(fromObject(t));
    }


    /**
     * swagger api 创建MONO
     *
     * @param t
     * @return
     */
    protected <T, E> Mono<ServerResponse> createMono(T t, E e) {
        return createMono(APPLICATION_JSON_UTF8, OK, t instanceof SwaggerResourcesProvider ? t : ofNullable(t).orElse((T) e));
    }

}
