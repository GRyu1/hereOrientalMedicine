package com.hereOM.backend.auth;

import com.hereOM.backend.domain.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails {

    private Member user;
    private Map<String, Object> attributes;

    public PrincipalDetails(Member user) {
        this.user = user;
    } // 일반로그인시 사용됨

    //OAuth2.0 로그인 시 사용됨.
    public PrincipalDetails(Member user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override//해당 유저의 권한을 리턴하는 곳!
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}