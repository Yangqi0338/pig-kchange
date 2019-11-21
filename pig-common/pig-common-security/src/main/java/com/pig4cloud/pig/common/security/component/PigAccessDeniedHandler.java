package com.pig4cloud.pig.common.security.component;

/**
 * @author lengleng
 * @date 2019/2/1
 */

import com.pig4cloud.pig.common.core.exception.PigDeniedException;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.WebUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.hutool.http.HttpStatus.HTTP_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author lengleng
 * 授权拒绝处理器，覆盖默认的OAuth2AccessDeniedHandler
 * 包装失败信息到PigDeniedException
 */
@Slf4j
@Component
public class PigAccessDeniedHandler extends OAuth2AccessDeniedHandler {

    /**
     * 授权拒绝处理，使用R包装
     *
     * @param request       request
     * @param response      response
     * @param authException authException
     */
    @Override
    @SneakyThrows
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) {

        log.info("授权失败，禁止访问 {}", request.getRequestURI());

        R<PigDeniedException> result = R.failed(new PigDeniedException("授权失败，禁止访问"));
        WebUtils.renderJson(response, result, APPLICATION_JSON_UTF8_VALUE, HTTP_FORBIDDEN, null);
    }
}
