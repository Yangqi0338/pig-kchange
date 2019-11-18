package com.pig4cloud.pig.common.swagger.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.*;

/**
 * 网关处理 swagger api
 *
 * @author lengleng
 * @date 2018-07-19
 * SwaggerUiHandler
 */
@Slf4j
@Component
public class SwaggerAPIHandler extends BaseHandler implements HandlerFunction<ServerResponse> {

    @Autowired(required = false)
    private UiConfiguration uiConfiguration;
    @Autowired(required = false)
    private SecurityConfiguration securityConfiguration;

    private final SwaggerResourcesProvider swaggerResources;

    public SwaggerAPIHandler(SwaggerResourcesProvider swaggerResources) {
        this.swaggerResources = swaggerResources;
    }

    /**
     * 对swagger api 请求处理
     *
     * @param request 对处理程序的请求
     * @return the response
     */
    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        // swagger api 访问处理
        Mono response = null;
        switch (request.path()) {
            case SWAGGER_RESOURCES:
                response = createMono(swaggerResources.get(), null);
                break;
            case SWAGGER_UI:
                response = createMono(uiConfiguration, UiConfigurationBuilder.builder().build());
                break;
            case SWAGGER_SECURITY:
                response = createMono(securityConfiguration, SecurityConfigurationBuilder.builder().build());
                break;
        }
        return response;
    }

}
