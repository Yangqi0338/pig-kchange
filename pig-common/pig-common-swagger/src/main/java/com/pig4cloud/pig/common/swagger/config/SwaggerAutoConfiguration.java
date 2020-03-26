package com.pig4cloud.pig.common.swagger.config;


import com.pig4cloud.pig.common.swagger.properties.SwaggerProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Predicates.*;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

/**
 * @author lengleng
 * swagger配置
 */
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "spring.swagger.enabled", matchIfMissing = true)
public class SwaggerAutoConfiguration {

    /**
     * 默认的排除路径，排除Spring Boot默认的错误处理路径和端点
     */
    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");
    private static final String BASE_PATH = "/**";

    public SwaggerProperties swaggerProperties;


    @Bean
    public Docket api() {
        // base-path处理
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add(BASE_PATH);
        }

        // exclude-path处理
        if (swaggerProperties.getExcludePath().isEmpty()) {
            swaggerProperties.getExcludePath().addAll(DEFAULT_EXCLUDE_PATH);
        }

        //noinspection Guava
        return new Docket(SWAGGER_2).host(swaggerProperties.getHost()).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(and(not(or(swaggerProperties.getExcludePath().stream().map(it -> ant(it)).collect(toList()))),
                        or(swaggerProperties.getBasePath().stream().map(it -> ant(it)).collect(toList()))))
                .build().securitySchemes(singletonList(securitySchema()))
                .securityContexts(singletonList(securityContext())).pathMapping("/");
    }

    /**
     * 配置默认的全局鉴权策略的开关，通过正则表达式进行匹配；默认匹配所有URL
     *
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(regex(swaggerProperties.getAuthorization().getAuthRegex()))
                .build();
    }

    /**
     * 默认的全局鉴权策略
     *
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopeList = swaggerProperties.getAuthorization().getAuthorizationScopeList().stream()
                .map(it -> new AuthorizationScope(it.getScope(), it.getDescription())).collect(toList()).stream().toArray(AuthorizationScope[]::new);
        return singletonList(SecurityReference.builder()
                .reference(swaggerProperties.getAuthorization().getName())
                .scopes(authorizationScopeList).build());
    }


    private OAuth securitySchema() {
        List<AuthorizationScope> authorizationScopeList = swaggerProperties.getAuthorization().getAuthorizationScopeList().stream()
                .map(it -> new AuthorizationScope(it.getScope(), it.getDescription())).collect(toList());
        List<GrantType> grantTypes = swaggerProperties.getAuthorization().getTokenUrlList().stream()
                .map(it -> new ResourceOwnerPasswordCredentialsGrant(it)).collect(toList());
        return new OAuth(swaggerProperties.getAuthorization().getName(), authorizationScopeList, grantTypes);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(),
                        swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

}
