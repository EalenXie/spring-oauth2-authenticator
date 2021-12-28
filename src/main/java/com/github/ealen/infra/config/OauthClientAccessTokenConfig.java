package com.github.ealen.infra.config;

import com.github.ealen.domain.vo.AccountInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author EalenXie create on 2020/11/3 11:46
 */
@Configuration
public class OauthClientAccessTokenConfig {


    /**
     * 前面的 jwt key 我这里写死为 5371f568a45e5ab1f442c38e0932aef24447139b
     */
    private static final String SIGNING_KEY = "5371f568a45e5ab1f442c38e0932aef24447139b";

    @Resource
    private DataSource dataSource;

    /**
     * 声明 ClientDetails实现
     *
     * @return ClientDetailsService
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }



    /**
     * 配置TokenStore token持久化
     */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
    /**
     * tokenService 配置
     */
    @Bean(name = "tokenServices")
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(jdbcClientDetailsService());
        // 允许支持refreshToken
        tokenServices.setSupportRefreshToken(true);
        // refreshToken 不重用策略
        tokenServices.setReuseRefreshToken(false);
        //设置Token存储方式
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setTokenEnhancer(tokenEnhancerChain());
        return tokenServices;
    }




    /**
     * 自定义TokenEnhancerChain 由多个TokenEnhancer组成
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter(), additionalInformationTokenEnhancer()));
        return tokenEnhancerChain;
    }

    /**
     * JWT 转换器
     */
    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }

    /**
     * token 额外自定义信息 此例获取用户信息
     */
    @Bean
    public TokenEnhancer additionalInformationTokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> information = new HashMap<>(8);
            Authentication userAuthentication = authentication.getUserAuthentication();
            if (userAuthentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) userAuthentication;
                Object principal = token.getPrincipal();
                if (principal instanceof OauthAccountUserDetails) {
                    OauthAccountUserDetails userDetails = (OauthAccountUserDetails) token.getPrincipal();
                    AccountInfo accountInfo = new AccountInfo();
                    BeanUtils.copyProperties(userDetails.getOauthAccount(), accountInfo);
                    information.put("account_info", accountInfo);
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(information);
                }
            }
            return accessToken;
        };
    }
}
