package com.pig4cloud.pig.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author lengleng
 * @date 2019/2/1
 * 路由限流配置
 */
@Configuration
public class RateLimiterConfiguration {

    /**
     * IP限流
     *
     * @return
     */
    @Bean("remoteAddrKeyResolver")
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

    /**
     * 接口限流
     *
     * @return
     */
//    @Bean("apiKeyResolver")
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    /**
     * 用户限流
     *
     * @return
     */
//    @Bean("userKeyResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }
}
