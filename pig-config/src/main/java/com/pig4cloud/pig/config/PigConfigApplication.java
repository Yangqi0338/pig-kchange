package com.pig4cloud.pig.config;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author lengleng
 * @date 2018年06月22日
 * 配置中心
 */
@EnableConfigServer
@SpringCloudApplication
public class PigConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigConfigApplication.class, args);
	}
}
