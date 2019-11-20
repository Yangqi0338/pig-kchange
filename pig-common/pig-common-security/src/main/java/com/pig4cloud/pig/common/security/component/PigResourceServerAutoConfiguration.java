package com.pig4cloud.pig.common.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.Charsets;
import net.dreamlu.mica.http.LbRestTemplate;
import net.dreamlu.mica.http.RestTemplateHeaderInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author lengleng
 * @date 2019/03/08
 */
@Slf4j
@Configuration
@ConditionalOnClass(OkHttpClient.class)
@RequiredArgsConstructor
@ComponentScan
public class PigResourceServerAutoConfiguration {

	private final ApplicationContext context;

	/**
	 * 支持负载均衡的 RestTemplate
	 *
	 * @param okHttpClient OkHttpClient
	 * @return LbRestTemplate
	 */
	@Bean
	@LoadBalanced
	@ConditionalOnMissingBean
	public RestTemplate lbRestTemplate(RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient) {
		log.info("init：RestTemplate。。。。。。。。。 {}", restTemplateBuilder);
		restTemplateBuilder.requestFactory(() -> new OkHttp3ClientHttpRequestFactory(okHttpClient));
		LbRestTemplate restTemplate = restTemplateBuilder.build(LbRestTemplate.class);
		restTemplate.getInterceptors().add(context.getBean(RestTemplateHeaderInterceptor.class));
		configMessageConverters(context, restTemplate.getMessageConverters());
		return restTemplate;
	}

	private static void configMessageConverters(ApplicationContext context, List<HttpMessageConverter<?>> converters) {
		converters.removeIf(x -> x instanceof StringHttpMessageConverter || x instanceof MappingJackson2HttpMessageConverter);
		converters.add(new StringHttpMessageConverter(Charsets.UTF_8));
		converters.add(new MappingJackson2HttpMessageConverter(context.getBean(ObjectMapper.class)));
	}


/*	@Bean
	@Primary
	@LoadBalanced
	public RestTemplate lbRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(
				new DefaultResponseErrorHandler() {
			@Override
			@SneakyThrows
			public void handleError(ClientHttpResponse response) {
				if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
					super.handleError(response);
				}
			}
		});
		return restTemplate;
	}*/

}
