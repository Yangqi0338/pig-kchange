package com.pig4cloud.pig.auth;


import com.pig4cloud.pig.common.security.annotation.EnableSpringBootAntCloudExcludeResou;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author lengleng
 * @date 2018年06月21日
 * 认证授权中心
 */
@EnableSpringBootAntCloudExcludeResou()
@SpringCloudApplication
public class PigAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigAuthApplication.class, args);
	}
}
