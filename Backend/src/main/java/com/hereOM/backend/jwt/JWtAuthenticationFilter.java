package com.hereOM.backend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hereOM.backend.Dto.MemberLoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JWtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("LoginStart @ JWtAuthenticationFilter");
        ObjectMapper objectMapper = new ObjectMapper();
        MemberLoginDto memberLoginDto = null;

        return null;
    }
}
