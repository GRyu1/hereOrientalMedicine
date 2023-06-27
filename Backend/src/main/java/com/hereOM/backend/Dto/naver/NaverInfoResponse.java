package com.hereOM.backend.Dto.naver;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NaverInfoResponse {
    private String resultcode;
    private String message;
    private NaverUserInfo response;
}
