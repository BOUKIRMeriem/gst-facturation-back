package com.facturation.backend_facturation.document;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "accounts")
public class Account {

    @Id
    private String id;
    private User user;
    private String email;
    private String password;
    private boolean verified;
    private boolean enabled;
    private boolean locked;
}
