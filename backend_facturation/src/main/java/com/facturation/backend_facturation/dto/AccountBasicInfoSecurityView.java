package com.facturation.backend_facturation.dto;


public interface AccountBasicInfoSecurityView {

    String getId();
    String getEmail();
    UserBasicInfoSecurityView getUser();
    boolean isVerified();
}
