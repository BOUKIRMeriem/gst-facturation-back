package com.facturation.backend_facturation.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="d_detail_facture_client")
public class DetailFactureClientDocument {

    @Id
    private String id;

    private int rating;

    private FactureClientDocument factureClient;

    private ProductDocument product;

    private Double tva;

    private Double price;

    private Double quantity;

    private String description;

    private Double total;

    public String getId() {
        return id;
    }

    public ProductDocument getProduct() {
        return product;
    }

    public Double getTva() {
        return tva;
    }

    public Double getPrice() {
        return price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getTotal() {
        return total;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }
}
