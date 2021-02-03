package com.github.ealen.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ealen.domain.vo.RespBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsUtils;

import javax.annotation.Resource;

/**
 * @author EalenXie create on 2020/11/3 13:00
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private OauthAccountUserDetailsService oauthAccountUserDetailsService;


    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 这些接口 对于认证中心来说无需授权
     */
    protected static final String[] PERMIT_ALL_URL = {"/oauth/**", "/user/**", "/actuator/**", "/error", "/open/api"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and().csrf().disable()
                .authorizeRequests()
                //处理跨域请求中的Preflight请求
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(PERMIT_ALL_URL)
                .permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .and().logout()
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .and().httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录成功处理器
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            RespBody<Void> resp = new RespBody<>(HttpStatus.OK.value(), "login success", null);
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(resp));
        };
    }

    /**
     * 登出成功处理器
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            RespBody<Void> resp = new RespBody<>(HttpStatus.OK.value(), "logout success", null);
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(resp));
        };
    }

    /**
     * 常规登录失败处理器
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            RespBody<Void> resp = new RespBody<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null);
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(resp));
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(oauthAccountUserDetailsService).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(true);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
