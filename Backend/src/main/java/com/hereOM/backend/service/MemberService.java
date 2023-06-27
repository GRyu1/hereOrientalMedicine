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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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
    public String getGoogleInfo(String authCode){
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

        System.out.println(response.toString());

        String jwtToken=response.getBody().getId_token();
        Map<String, String> map=new HashMap<>();
        map.put("id_token",jwtToken);

        ResponseEntity<GoogleInfoResponse> infoResponse = restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",
                map, GoogleInfoResponse.class);

        System.out.println(infoResponse.getBody().toString());
        String email=infoResponse.getBody().getEmail();

        return email;
    }

    @Transactional
    public String getNaverAccessToken(String authCode, String state) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NaverResponse> response = restTemplate.getForEntity(String.format("https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=%s&client_secret=%s&state=%s&code=%s",naverClientId,naverClientSecret,state,authCode), NaverResponse.class);

        String jwtToken=response.getBody().getAccess_token();
        return jwtToken;
    }

    @Transactional
    public ResponseEntity<NaverInfoResponse> getNaverInfo(String code, String state) {
        System.out.println("GetNaverInfo Running");
        String accessToken = getNaverAccessToken(code, state);
        System.out.println("accessToken @ getNaverInfo"+accessToken);
        String header = "Bearer "+accessToken;
        String apiURL = "https://openapi.naver.com/v1/nid/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", header);

        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiURL));
        ResponseEntity<NaverInfoResponse> naverInfoResponseResponseEntity
                = restTemplate.exchange(requestEntity, NaverInfoResponse.class);

        System.out.println(naverInfoResponseResponseEntity.getBody().toString());

        return null;
    }
}