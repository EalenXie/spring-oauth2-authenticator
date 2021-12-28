package com.github.ealen.infra.config;

import com.github.ealen.domain.entity.OauthAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author EalenXie create on 2020/11/24 15:09
 */
public class OauthAccountUserDetails implements UserDetails {


    public OauthAccountUserDetails(OauthAccount oauthAccount, Collection<? extends GrantedAuthority> authorities) {
        this.oauthAccount = oauthAccount;
        this.authorities = authorities;
    }

    private final OauthAccount oauthAccount;

    private final Collection<? extends GrantedAuthority> authorities;

    public OauthAccount getOauthAccount() {
        return oauthAccount;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return oauthAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return oauthAccount.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return oauthAccount.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return oauthAccount.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return oauthAccount.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return oauthAccount.getEnabled();
    }
}
