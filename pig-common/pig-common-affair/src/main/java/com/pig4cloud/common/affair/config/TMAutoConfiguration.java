/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pig4cloud.common.affair.config;

import com.codingapi.txlcn.common.runner.TxLcnApplicationRunner;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.common.util.id.ModIdProvider;
import com.codingapi.txlcn.logger.TxLoggerConfiguration;
import com.codingapi.txlcn.txmsg.MessageConfiguration;
import com.pig4cloud.common.affair.interfaces.FastStorage;
import com.pig4cloud.common.affair.interfaces.FastStorageProvider;
import com.pig4cloud.common.affair.properties.TxManagerProperties;
import com.pig4cloud.common.affair.vo.RedisStorage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Date: 19-1-24 下午3:37
 *
 * @author ujued
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import({TxLoggerConfiguration.class, MessageConfiguration.class})
@EnableConfigurationProperties(TxManagerProperties.class)
@EnableJpaRepositories("com.pig4cloud.common.affair.*")
@EntityScan("com.pig4cloud.common.affair.*")
public class TMAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService executorService() {
        int coreSize = Runtime.getRuntime().availableProcessors() * 2;
        return new ThreadPoolExecutor(coreSize, coreSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()) {
            @Override
            public void shutdown() {
                super.shutdown();
                try {
                    this.awaitTermination(10, TimeUnit.MINUTES);
                } catch (InterruptedException ignored) {
                }
            }
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public FastStorageProvider fastStorageProvider(RedisTemplate<String, Object> redisTemplate,
                                                   StringRedisTemplate stringRedisTemplate, TxManagerProperties managerConfig) {
        return () -> new RedisStorage(redisTemplate, stringRedisTemplate, managerConfig);
    }

    @Bean
    public FastStorage fastStorage(FastStorageProvider fastStorageProvider) {
        return fastStorageProvider.provide();
    }

    @Bean
    public TxLcnApplicationRunner txLcnApplicationRunner(ApplicationContext applicationContext) {
        return new TxLcnApplicationRunner(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public ModIdProvider modIdProvider(ConfigurableEnvironment environment, ServerProperties serverProperties) {
        return () -> ApplicationInformation.modId(environment, serverProperties);
    }
}
