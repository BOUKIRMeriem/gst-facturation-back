package com.facturation.backend_facturation.controller.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;



@Data
@AllArgsConstructor
@Builder
public class NewAccountRequest {

    private String email;
    private String password;
    private com.facturation.backend_facturation.controller.requests.NewUserRequest user;
}
