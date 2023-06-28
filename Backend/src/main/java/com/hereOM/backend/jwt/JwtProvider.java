package com.hereOM.backend.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hereOM.backend.Dto.TokenDto;
import com.hereOM.backend.domain.Member;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Component
public class JwtProvider {
    public static final String AUTHORITIES_KEY = JwtProperties.AUTHORITIES_KEY;
    public static final String BEARER_TYPE = JwtProperties.BEARER_TYPE;


    public TokenDto generateTokenDto(Member member){

        Date accessTokenExpiresIn = new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME);

        String accessToken = JWT.create().withSubject(member.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id",member.getId())
                .withClaim("username",member.getUsername())
                .withClaim("AUTHORITIES_KEY",member.getRole())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
    public boolean validateToken(String token) {

        try{
            Jwts.parserBuilder()
                    .setSigningKey(JwtProperties.SECRET)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("JWT 서명의 형식이 잘못되었습니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원하지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("걍.. 잘못된 JWT 입니다.");
        }
        return false;
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(JwtProperties.SECRET)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
