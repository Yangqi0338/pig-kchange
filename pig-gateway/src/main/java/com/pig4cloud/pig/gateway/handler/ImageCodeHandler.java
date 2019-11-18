package com.pig4cloud.pig.gateway.handler;

import com.google.code.kaptcha.Producer;
import com.pig4cloud.pig.common.swagger.handler.BaseHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.pig4cloud.pig.common.core.constant.CommonConstants.DEFAULT_CODE_KEY;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG;

/**
 * @author lengleng
 * @date 2019/2/1
 * 验证码生成逻辑处理类
 */
@Slf4j
@Component
@AllArgsConstructor
public class ImageCodeHandler extends BaseHandler {
    private final Producer producer;
    private final RedisTemplate redisTemplate;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        //生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);

        //保存验证码信息
        String randomStr = serverRequest.queryParam("randomStr").get();
        redisTemplate.opsForValue().set(DEFAULT_CODE_KEY + randomStr, text, 60, SECONDS);

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", os);
        } catch (IOException e) {
            log.error("ImageIO write error", e);
            return Mono.error(e);
        }

        return createMono(IMAGE_JPEG, OK, new ByteArrayResource(os.toByteArray()));
    }
}
