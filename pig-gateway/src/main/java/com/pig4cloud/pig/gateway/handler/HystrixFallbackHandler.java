package com.pig4cloud.pig.gateway.handler;

import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.swagger.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

/**
 * @author lengleng
 * @date 2019/2/1
 * Hystrix 降级处理
 */
@Slf4j
@Component
public class HystrixFallbackHandler extends BaseHandler {
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        Optional<Object> originalUris = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

        originalUris.ifPresent(originalUri -> log.error("网关执行请求:{}失败,hystrix服务降级处理", originalUri));

        return createMono(APPLICATION_JSON_UTF8, INTERNAL_SERVER_ERROR, R.failed("服务器异常"));
    }
}
