
package com.pig4cloud.nacos.config;

import com.pig4cloud.nacos.filter.JwtAuthenticationTokenFilter;
import com.pig4cloud.nacos.security.CustomUserDetailsServiceImpl;
import com.pig4cloud.nacos.security.JwtAuthenticationEntryPoint;
import com.pig4cloud.nacos.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.springframework.security.config.BeanIds.AUTHENTICATION_MANAGER;

/**
 * Spring security config
 *
 * @author Nacos
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String AUTHORIZATION_TOKEN = "access_token";

    public static final String SECURITY_IGNORE_URLS_SPILT_CHAR = ",";

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtTokenUtils tokenProvider;

    @Autowired
    private Environment env;

    @Bean(name = AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        String ignoreURLs = env.getProperty("nacos.security.ignore.urls", "/**");
        web.ignoring().antMatchers(Stream.of(ignoreURLs.trim().split(SECURITY_IGNORE_URLS_SPILT_CHAR)).collect(joining(",")).split(","));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated().and()
                // custom token authorize exception handler
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler).and()
                // since we use jwt, session is not necessary
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // since we use jwt, csrf is not necessary
                .csrf().disable()
                // 自定义过滤器
                .addFilterBefore(new JwtAuthenticationTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                // disable cache
                .headers().cacheControl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
