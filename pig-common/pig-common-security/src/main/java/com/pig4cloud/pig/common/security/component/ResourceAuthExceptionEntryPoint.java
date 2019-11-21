package com.pig4cloud.pig.common.security.component;

import com.google.gson.Gson;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.hutool.http.HttpStatus.HTTP_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author lengleng
 * @date 2019/2/1
 * 客户端异常处理
 * 1. 可以根据 AuthenticationException 不同细化异常处理
 */
@Slf4j
@Component
@AllArgsConstructor
public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {
    private final Gson gson;

    @Override
    @SneakyThrows
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {

        R result = R.builder().build();

        if (authException != null) {
            result.setMsg("error").setData(authException.getMessage());
        }

        WebUtils.renderJson(response, result, APPLICATION_JSON_UTF8_VALUE, HTTP_UNAUTHORIZED, null);

    }
}
