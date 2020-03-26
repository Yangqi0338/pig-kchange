package com.pig4cloud.pig.common.swagger.config;

import com.pig4cloud.pig.common.core.config.FilterIgnorePropertiesConfig;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.springframework.cloud.gateway.support.NameUtils.GENERATED_NAME_PREFIX;

/**
 * @author Sywd
 * 聚合接口文档注册，和zuul实现类似
 */
@Primary
@Component
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {
    private static final String API_URI = "/v2/api-docs";
    private final RouteLocator routeLocator;
    private final FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;
    private final GatewayProperties gatewayProperties;

    @Override
    public List<SwaggerResource> get() {
        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));

        List<SwaggerResource> result = gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.stream().collect(toList())
                .contains(routeDefinition.getId())).collect(toList()).stream().flatMap(it ->
                it.getPredicates().stream().filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        // 过滤数据
                        .filter(predicateDefinition -> !filterIgnorePropertiesConfig.getSwaggerProviders().contains(it.getId())).map(predicateDefinition ->
                        swaggerResource(it.getId(), predicateDefinition.getArgs().get(GENERATED_NAME_PREFIX + "0").replace("/**", API_URI)))
        ).sorted(comparing(SwaggerResource::getName)).collect(toList());

        return result;
    }

    private static SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
