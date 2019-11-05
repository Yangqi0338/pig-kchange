package com.pig4cloud.pig.eureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author lengleng
 * @date 2018年06月21日
 * 服务中心
 */
@EnableEurekaServer
@SpringBootApplication
public class PigEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigEurekaApplication.class, args);
	}
}
