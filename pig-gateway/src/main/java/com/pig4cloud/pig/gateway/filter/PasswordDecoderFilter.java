package com.pig4cloud.pig.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static cn.hutool.core.util.CharsetUtil.UTF_8;
import static cn.hutool.core.util.StrUtil.containsAnyIgnoreCase;
import static cn.hutool.core.util.StrUtil.isNotBlank;
import static cn.hutool.crypto.Mode.CBC;
import static cn.hutool.crypto.Padding.NoPadding;
import static cn.hutool.http.HttpUtil.toParams;
import static com.pig4cloud.pig.common.core.constant.SecurityConstants.OAUTH_TOKEN_URL;

/**
 * @author lengleng
 * @date 2019/2/1
 * 密码解密工具类
 */
@Slf4j
@Component
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory {
	private static final String PASSWORD = "password";
	private static final String KEY_ALGORITHM = "AES";
	@Value("${security.encode.key:1234567812345678}")
	private String encodeKey;

	private static String decryptAES(String data, String pass) {

		AES aes = new AES(CBC, NoPadding,new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM),
				new IvParameterSpec(pass.getBytes()));

		byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));

		return new String(result, StandardCharsets.UTF_8);
	}

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			// 不是登录请求，直接向下执行
			if (!containsAnyIgnoreCase(request.getURI().getPath(), OAUTH_TOKEN_URL)) {
				return chain.filter(exchange);
			}

			URI uri = exchange.getRequest().getURI();
			Map<String, String> paramMap = HttpUtil.decodeParamMap(uri.getRawQuery(), UTF_8);

			String password = paramMap.get(PASSWORD);
			if (isNotBlank(password)) {
				try {
					password = decryptAES(password, encodeKey);
				} catch (Exception e) {
					log.error("密码解密失败:{}", password);
					return Mono.error(e);
				}
				paramMap.put(PASSWORD, password.trim());
			}

			URI newUri = UriComponentsBuilder.fromUri(uri).replaceQuery(toParams(paramMap)).build(true).toUri();

			ServerHttpRequest newRequest = exchange.getRequest().mutate().uri(newUri).build();
			return chain.filter(exchange.mutate().request(newRequest).build());
		};
	}
}
