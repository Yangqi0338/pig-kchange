package com.pig4cloud.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.pig4cloud.nacos.config.ConfigConstants.*;

/**
 * @author nacos
 * <p>
 * nacos console 源码运行，方便开发
 * 生产建议从官网下载最新版配置运行
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.*.nacos"})
public class PigNacosApplication {

	public static void main(String[] args) {
		System.setProperty(TOMCAT_DIR, "logs");
		System.setProperty(TOMCAT_ACCESS_LOG, "false");
		System.setProperty(STANDALONE_MODE, "true");
		SpringApplication.run(PigNacosApplication.class, args);
	}
}
