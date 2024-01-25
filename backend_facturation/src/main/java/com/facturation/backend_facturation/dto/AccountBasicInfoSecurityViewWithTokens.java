package com.facturation.backend_facturation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
@Builder
public class AccountBasicInfoSecurityViewWithTokens {

    private String id;
    private UserBasicInfoSecurityView user;
    private String email;
    private boolean verified;
    private String accessToken;
    private String refreshToken;
}
