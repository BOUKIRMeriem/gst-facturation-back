package com.facturation.backend_facturation.document;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="t_client")
public class ClientDocument {
    @Id
    private String id;
    private String ice;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Double solde;
}
