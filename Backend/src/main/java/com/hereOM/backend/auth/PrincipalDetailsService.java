package com.hereOM.backend.auth;

import com.hereOM.backend.domain.Member;
import com.hereOM.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username/*로그인 form 의 파라미터*/) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadUserByUsername메소드");
        System.out.println("loadUserByname : "+username);
        Member userEntity = memberRepository.findByUsername(username);
        if(userEntity != null){
            System.out.println("로그인 성공 user : "+userEntity.toString());
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}