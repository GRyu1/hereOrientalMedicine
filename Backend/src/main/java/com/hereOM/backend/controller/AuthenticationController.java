package com.hereOM.backend.controller;

import com.hereOM.backend.Dto.MemberLoginDto;
import com.hereOM.backend.service.AuthenticationService;
import com.hereOM.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {
    public final AuthenticationService authenticationService;
    public final MemberService memberService;

    @GetMapping("/login")
    public String login(@RequestParam String code) {
        String email = memberService.getGoogleInfo(code);
        System.out.println("login@Controller : "+code);
//        TokenDto tokenDto = securityService.login(email);
//        HttpHeaders headers = securityService.setTokenHeaders(tokenDto);

        return email;
    }
}
