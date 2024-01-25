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
@Document(collection = "d_product")
public class ProductDocument {

    @Id
    private String id;
    private String reference;
    private String name;
    private Double purchasePrice;
    private Double sellingPrice;
    private Double departureQuantity;
    private Double totalQuantity;

}
