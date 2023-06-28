package com.hereOM.backend.controller;

import com.hereOM.backend.Dto.TokenDto;
import com.hereOM.backend.domain.Member;
import com.hereOM.backend.service.AuthenticationService;
import com.hereOM.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {
    public final MemberService memberService;
    public final AuthenticationService authenticationService;

    @GetMapping("/login/google")
    public ResponseEntity login(String code) {

        Member member = memberService.getGoogleInfo(code);
        TokenDto tokenDto = authenticationService.login(member);
        HttpHeaders headers = authenticationService.setTokenHeaders(tokenDto);

//        System.out.println("googleLoginSecurityContextHolder"+SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().headers(headers).body("accessToken: "+tokenDto.getAccessToken());
    }

    @GetMapping("/login/naver")
    public ResponseEntity login(String code, String state) {
        Member member = memberService.getNaverInfo(code,state);
        TokenDto tokenDto = authenticationService.login(member);
        HttpHeaders headers = authenticationService.setTokenHeaders(tokenDto);

//        System.out.println("naverLoginSecurityContextHolder"+SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().headers(headers).body("accessToken: "+tokenDto.getAccessToken());
    }
}
