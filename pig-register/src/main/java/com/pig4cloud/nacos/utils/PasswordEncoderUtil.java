
package com.pig4cloud.nacos.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 生成密码
 *
 * @author nacos
 */
public class PasswordEncoderUtil {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("nacos"));
     }

    public static Boolean matches(String raw, String encoded) {
        return new BCryptPasswordEncoder().matches(raw, encoded);
    }

    public static String encode(String raw) {
        return new BCryptPasswordEncoder().encode(raw);
    }
}
