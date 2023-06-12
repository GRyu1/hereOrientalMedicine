package com.hereOM.backend.auth;

import com.hereOM.backend.domain.Member;
import com.hereOM.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private MemberRepository memberRepository;

    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글 로그인 버튼 클릭 -> 로그인창 -> 로근인을 완료 -> code를 리턴(OAuth-Client라이브러리) -> AccessToken 요청
        //userRequest 정보 -> 회원프로필 받아야함 (loadUser함수) 호출 -> 구글로 부터 회원프로필을 받아줌.
        NaverUserInfo naverUserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));

        String provider = naverUserInfo.getProvider();
        String providerId = naverUserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("여기가한의원");
        String email = naverUserInfo.getEmail();
        String role = "ROLE_USER";
        // 회원가입 진행.
        Member userEntity = this.memberRepository.findByUsername(username);
        if (userEntity == null) {
            System.out.println(provider + "로그인이 최초입니다.");
            userEntity = Member.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId).build();
            memberRepository.save(userEntity);
        } else {
            System.out.println("회원가입이 되어있는 회원 입니다.");
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}