package com.pig4cloud.pig.common.security.feign;

import com.netflix.hystrix.HystrixCommand;
import feign.Contract;
import feign.Feign;
import feign.RequestInterceptor;
import feign.hystrix.HystrixFeign;
import lombok.AllArgsConstructor;
import net.dreamlu.mica.core.convert.EnumToStringConverter;
import net.dreamlu.mica.core.convert.StringToEnumConverter;
import net.dreamlu.mica.feign.MicaFeignAutoConfiguration;
import net.dreamlu.mica.feign.MicaFeignRequestHeaderInterceptor;
import net.dreamlu.mica.feign.MicaSpringMvcContract;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.MicaFeignClientsRegistrar;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.context.annotation.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import java.util.ArrayList;

/**
 * @author lengleng
 * @date 2019/2/1
 * feign 拦截器传递 header 中oauth token，
 * 使用hystrix 的信号量模式
 */
@Configuration
@ConditionalOnClass(Feign.class)
@Import(MicaFeignClientsRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
@EnableAutoConfiguration(exclude = {MicaFeignAutoConfiguration.class})
@AllArgsConstructor
public class PigFeignClientConfiguration {

	@Autowired
	private OAuth2ProtectedResourceDetails details;
	@Autowired
	private AccessTokenContextRelay relay;

	@Bean
	@ConditionalOnMissingBean
	public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext) {
		return new PigFeignClientInterceptor(oAuth2ClientContext, details, relay);
	}

	@Configuration("hystrixFeignConfiguration")
	@ConditionalOnClass({HystrixCommand.class, HystrixFeign.class})
	protected static class HystrixFeignConfiguration {

		@Bean
		@Primary
		@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
		@ConditionalOnProperty(value = "feign.hystrix.enabled", matchIfMissing = true)
		public Feign.Builder feignHystrixBuilder(@Qualifier("oauth2FeignRequestInterceptor")
														 RequestInterceptor requestInterceptor, Contract feignContract) {
			return HystrixFeign.builder().contract(feignContract)
					.decode404().requestInterceptor(requestInterceptor);
		}

	}

	/**
	 * mica enum 《-》 String 转换配置
	 *
	 * @param objectProvider ObjectProvider
	 * @return SpringMvcContract
	 */
	@Bean
	public Contract feignContract(@Qualifier("mvcConversionService") ObjectProvider<ConversionService> objectProvider) {
		ConversionService conversionService = objectProvider.getIfAvailable(DefaultConversionService::new);
		ConverterRegistry converterRegistry = ((ConverterRegistry) conversionService);
		converterRegistry.addConverter(new EnumToStringConverter());
		converterRegistry.addConverter(new StringToEnumConverter());
		return new MicaSpringMvcContract(new ArrayList<>(), conversionService);
	}
}
