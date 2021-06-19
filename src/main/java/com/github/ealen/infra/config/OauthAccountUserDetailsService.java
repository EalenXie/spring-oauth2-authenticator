package com.github.ealen.infra.config;


import com.github.ealen.domain.entity.OauthAccount;
import com.github.ealen.domain.mapper.OauthAccountMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author EalenXie create on 2020/11/24 15:15
 */
@Service
public class OauthAccountUserDetailsService implements UserDetailsService {

    @Resource
    private OauthAccountMapper oauthAccountMapper;

    private final BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();
    @Resource
    private JdbcClientDetailsService jdbcClientDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientId;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User clientUser = (User) principal;
                clientId = clientUser.getUsername();
            } else if (principal instanceof OauthAccountUserDetails) {
                getClientIdByRequest();
                return (OauthAccountUserDetails) principal;
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            clientId = getClientIdByRequest();
        }
        // 获取用户
        OauthAccount account = oauthAccountMapper.loadUserByUsername(clientId, username);
        // 用户不存在
        if (account == null || !account.getAccountNonDeleted()) {
            throw new UsernameNotFoundException("user not found");
        }
        // 授权
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new OauthAccountUserDetails(account, authorities);
    }


    /**
     * 从httpRequest中获取并验证客户端信息
     */
    public String getClientIdByRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) throw new UnsupportedOperationException();
        HttpServletRequest request = attributes.getRequest();
        UsernamePasswordAuthenticationToken client = authenticationConverter.convert(request);
        if (client == null) {
            throw new UnauthorizedClientException("unauthorized client");
        }
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(client.getName());
        if (!passwordEncoder.matches((String) client.getCredentials(), clientDetails.getClientSecret())) {
            throw new BadClientCredentialsException();
        }
        return clientDetails.getClientId();
    }

}
