package com.pig4cloud;


import com.pig4cloud.pig.common.security.annotation.EnableSpringBootAntCloud;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author lengleng
 * @date 2018年06月21日
 * <p>
 * 事务处理
 */
@EnableSpringBootAntCloud
@SpringCloudApplication
public class PigAffairApplication {

    public static void main(String[] args) {
        SpringApplication.run(PigAffairApplication.class, args);
    }
}
