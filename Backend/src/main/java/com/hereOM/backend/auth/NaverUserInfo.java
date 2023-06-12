package com.hereOM.backend.auth;

import java.util.Map;

public class NaverUserInfo {
    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getProviderId() {
        return (String) attributes.get("id");
    }

    public String getProvider() {
        return "Naver";
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getName() {
        return (String) attributes.get("name");
    }
}