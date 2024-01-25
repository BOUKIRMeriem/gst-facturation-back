package com.facturation.backend_facturation.controller.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;



@Data
@AllArgsConstructor
@Builder
public class NewUserRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;
}
