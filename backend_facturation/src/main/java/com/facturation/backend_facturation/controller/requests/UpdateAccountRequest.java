package com.facturation.backend_facturation.controller.requests;

import lombok.*;



@AllArgsConstructor
@Builder
@Data
public class UpdateAccountRequest {
    private String id;
    private String email;
    private NewUserRequest user;
}
