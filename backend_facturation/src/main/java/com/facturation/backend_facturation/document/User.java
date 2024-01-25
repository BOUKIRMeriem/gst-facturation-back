package com.facturation.backend_facturation.document;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;
    @Field(name = "first_name")
    private String firstName;
    @Field(name = "last_name")
    private String lastName;
    @Field(name = "user_name")
    private String userName;
    @Field(name = "phone_number")
    private String phoneNumber;
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
}
