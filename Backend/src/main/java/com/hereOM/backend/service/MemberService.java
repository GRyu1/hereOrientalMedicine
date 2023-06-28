package com.hereOM.backend.service;

import com.hereOM.backend.Dto.google.GoogleInfoResponse;
import com.hereOM.backend.Dto.google.GoogleRequest;
import com.hereOM.backend.Dto.google.GoogleResponse;
import com.hereOM.backend.Dto.naver.NaverInfoResponse;
import com.hereOM.backend.Dto.naver.NaverResponse;
import com.hereOM.backend.domain.Member;
import com.hereOM.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Value("${spring.google.client-id}")
    String googleClientId;

    @Value("${spring.google.client-secret}")
    String googleClientSecret;

    @Value("${spring.naver.client-id}")
    String naverClientId;

    @Value("${spring.naver.client-secret}")
    String naverClientSecret;


    @Transactional
    public Member getGoogleInfo(String authCode){
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(authCode)
                .redirectUri("http://localhost:3000")
                .grantType("authorization_code").build();

        ResponseEntity<GoogleResponse> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                googleOAuthRequestParam, GoogleResponse.class);

        System.out.println("GoogleResponse @ getGoogleInfo : "+response.toString());

        String jwtToken=response.getBody().getId_token();
        Map<String, String> map=new HashMap<>();
        map.put("id_token",jwtToken);

        ResponseEntity<GoogleInfoResponse> infoResponse = restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",
                map, GoogleInfoResponse.class);

        String email=infoResponse.getBody().getEmail();
        Member member= memberRepository.findByUsername("google"+email);
        if(member == null){
            System.out.println("최초 로그인->자동 회원가입 진행 @ getGoogleInfo.Controller");

            member = Member.builder()
                    .username("google"+email)
                    .email(email)
                    .name(infoResponse.getBody().getName())
                    .role("ROLE_USER")
                    .provider("google")
                    .build();
            memberRepository.save(member);
//            authenticationSaver(member);
            return member;
        }
        System.out.println("Member @ googleLogin"+member);
        if(member!=null){
//            authenticationSaver(member);
        }
        return member;
    }

    @Transactional
    public String getNaverAccessToken(String authCode, String state) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NaverResponse> response = restTemplate.getForEntity(String.format("https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=%s&client_secret=%s&state=%s&code=%s",naverClientId,naverClientSecret,state,authCode), NaverResponse.class);
        String jwtToken=response.getBody().getAccess_token();
        System.out.println("accessToken @ getNaverAccessToken : "+jwtToken);
        return jwtToken;
    }

    @Transactional
    public Member getNaverInfo(String code, String state) {
//        System.out.println("GetNaverInfo Running");
        String accessToken = getNaverAccessToken(code, state);
//        System.out.println("accessToken @ getNaverInfo"+accessToken);
        String header = "Bearer "+accessToken;
        String apiURL = "https://openapi.naver.com/v1/nid/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", header);
        System.out.println("header@getNaverInfo :"+header);

        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiURL));
        ResponseEntity<NaverInfoResponse> naverInfoResponseResponseEntity
                = restTemplate.exchange(requestEntity, NaverInfoResponse.class);

        String email = naverInfoResponseResponseEntity.getBody().getResponse().getEmail();
        Member member= memberRepository.findByUsername("naver"+email);
        if(member == null){
            System.out.println("최초 로그인->자동 회원가입 진행 @ getNaverInfo.Controller");
            String _birthday = naverInfoResponseResponseEntity.getBody().getResponse().getBirthyear()+"-"+naverInfoResponseResponseEntity.getBody().getResponse().getBirthday();
            Date birthday = Date.valueOf(_birthday);
            System.out.println(birthday);
            member = Member.builder()
                    .username("naver"+email)
                    .birthday(birthday)
                    .email(email)
                    .phone(naverInfoResponseResponseEntity.getBody().getResponse().getMobile())
                    .name(naverInfoResponseResponseEntity.getBody().getResponse().getName())
                    .role("ROLE_USER")
                    .provider("naver")
                    .build();
            memberRepository.save(member);
//            authenticationSaver(member);
            return member;

        }

        System.out.println("Member @ naverLogin"+member);
        if(member!=null){
//            authenticationSaver(member);
        }
        return member;
    }

    private void authenticationSaver(Member member) {
        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole());
        // 인증에 사용할 사용자 정보 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, Collections.singleton(authority));

        // SecurityContextHolder를 사용하여 Authentication 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}