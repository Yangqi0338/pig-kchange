package com.pig4cloud.pig.admin;


import com.pig4cloud.pig.common.security.annotation.EnableSpringBootAntCloud;
import com.pig4cloud.pig.common.swagger.annotation.EnableSwagger2Pro;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author lengleng
 * @date 2018年06月21日
 * 用户统一管理系统
 */

@EnableSwagger2Pro
@EnableSpringBootAntCloud
@SpringCloudApplication
public class PigAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(PigAdminApplication.class, args);
	}

}
