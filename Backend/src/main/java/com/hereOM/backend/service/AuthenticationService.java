package com.hereOM.backend.service;

import com.hereOM.backend.Dto.TokenDto;
import com.hereOM.backend.domain.Member;
import com.hereOM.backend.jwt.JwtProvider;
import com.hereOM.backend.repository.MemberRepository;
import com.hereOM.backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public TokenDto login(Member member) {
        TokenDto tokenDto = jwtProvider.generateTokenDto(member);
        System.out.println("login @ AuthenticationService : "+tokenDto.toString());
        return tokenDto;
    }

    public HttpHeaders setTokenHeaders(TokenDto tokenDto) {
        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = ResponseCookie.from("RefreshToken", tokenDto.getRefreshToken())
                .path("/")
                .maxAge(60*60*24*7) // 쿠키 유효기간 7일로 설정
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        headers.add("Set-cookie", cookie.toString());
        headers.add("Authorization", tokenDto.getAccessToken());

        return headers;
    }
}
