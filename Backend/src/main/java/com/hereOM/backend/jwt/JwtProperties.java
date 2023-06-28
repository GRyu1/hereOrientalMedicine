package com.hereOM.backend.jwt;

public class JwtProperties {
    static String SECRET = "CheongMyeon";
    static long EXPIRATION_TIME = 1000*60*10;
    static String TOKEN_PREFIX = "Bearer ";
    static String HEADER_STRING = "Authorization";
    static String BEARER_TYPE = "bearer";
    static String AUTHORITIES_KEY = "AUTHORITIES_KEY";
    static long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; //refresh 7일
}
