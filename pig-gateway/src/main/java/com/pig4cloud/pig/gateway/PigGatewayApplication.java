package com.pig4cloud.pig.gateway;


import com.pig4cloud.pig.common.swagger.annotation.EnableGatewaySwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author lengleng
 * @date 2018年06月21日
 * <p>
 * 网关应用
 */
@EnableGatewaySwagger2
@EnableAutoConfiguration(exclude = {JacksonAutoConfiguration.class})
@SpringCloudApplication
public class PigGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigGatewayApplication.class, args);
	}
}
