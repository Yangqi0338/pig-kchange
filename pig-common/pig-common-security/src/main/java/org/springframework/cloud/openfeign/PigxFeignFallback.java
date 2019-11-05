package org.springframework.cloud.openfeign;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author lengleng
 * <p>
 * fallback 代理处理
 */
@Slf4j
@AllArgsConstructor
public class PigxFeignFallback<T> implements MethodInterceptor {
	private final Class<T> targetType;
	private final String targetName;
	private final Throwable cause;

	@Nullable
	@Override
	@SneakyThrows
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
		Class<?> returnType = method.getReturnType();
		if (R.class != returnType) {
			return null;
		}
		FeignException exception = (FeignException) cause;

		byte[] content = exception.content();

		String str = StrUtil.str(content, StandardCharsets.UTF_8);

		log.error("PigxFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, str);
		ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);
		return objectMapper.readValue(str, R.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PigxFeignFallback<?> that = (PigxFeignFallback<?>) o;
		return targetType.equals(that.targetType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetType);
	}
}
