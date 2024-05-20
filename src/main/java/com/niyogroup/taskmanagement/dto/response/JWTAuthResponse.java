package com.niyogroup.taskmanagement.dto.response;

import lombok.Data;

@Data
public class JWTAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
}
