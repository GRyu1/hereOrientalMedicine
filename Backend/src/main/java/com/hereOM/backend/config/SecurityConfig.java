package com.hereOM.backend.config;

import com.hereOM.backend.auth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)//preAuthorize,PostAuthorize 어노테이션 활성화
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((crsf)-> crsf.disable());
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll());
        http
                .formLogin(form -> form
                        .loginPage("/loginForm")
                        .permitAll().loginProcessingUrl("/login").defaultSuccessUrl("/")
                ).oauth2Login((oauth2) -> oauth2.loginPage("/loginForm").userInfoEndpoint().userService(principalOauth2UserService));

//        http.authorizeHttpRequests((authorize) -> authorize
//                .requestMatchers("/admin").hasRole("ADMIN")
//        );
//                .requestMatchers("/user/**").authenticated()
//                .requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN") //시큐리티 설정은 "ADMIN" <=> USER DB 에는 "ROLE_ADMIN"
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/loginForm")
//                .loginProcessingUrl("/login") // login주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌. >> 컨트롤러에서 로그인맵핑이 필요없다.
//                .defaultSuccessUrl("/")
//                .and()
//                .oauth2Login()
//                .loginPage("/loginForm")
//                .userInfoEndpoint()
//                .userService(principalOauth2UserService);


        return http.build();
    }

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder PWencoder() {
        return new BCryptPasswordEncoder();
    }
}