package com.hereOM.backend.Dto.naver;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NaverUserInfo {
    private String email;
    private String nickname;
    private String profile_image;
    private String age;
    private String gender;
    private String id;
    private String name;
    private String birthday;
    private String birthyear;
    private String mobile;

}
