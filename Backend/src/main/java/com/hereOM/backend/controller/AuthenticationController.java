package com.hereOM.backend.controller;

import com.hereOM.backend.Dto.naver.NaverInfoResponse;
import com.hereOM.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {
    public final MemberService memberService;

    @GetMapping("/login/google")
    public String login(String code) {
        String email = memberService.getGoogleInfo(code);
        return email;
    }

    @GetMapping("/login/naver")
    public ResponseEntity<NaverInfoResponse> login(String code, String state) {
        ResponseEntity<NaverInfoResponse> result = memberService.getNaverInfo(code,state);
        return result;
    }
}
