package com.facturation.backend_facturation.document;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Set;

@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="d_facture_client")
public class FactureClientDocument {

    @Id
    private String id;

    @Field(name = "num_piece")
    private Long numPiece;

    @Field(name = "num_origin")
    private Long numOrigin;

    private Date date;

    @Field(name = "client_id")
    private ClientDocument client;

    @Field(name = "total_ttc")
    private Double totalTTC;

    @Field(name = "total_tva")
    private Double totalTVA;

    @Field(name = "total_ht")
    private Double totalHT;

    @Field(name = "total_discount")
    private Double totalDiscount;

    private String type;

    private boolean verified = false;

    private int year;

    @Field(name = "total_amount_payment")
    private Double totalAmountPayment;

    @Field(name = "details_facture")
    private Set<DetailFactureClientDocument> detailsFacture;

    @Field(name = "details_payment")
    private Set<PaymentClientDocument> detailsPayment;

    public String getId() {
        return id;
    }

    public Long getNumPiece() {
        return numPiece;
    }

    public Long getNumOrigin() {
        return numOrigin;
    }

    public Date getDate() {
        return date;
    }

    public ClientDocument getClient() {
        return client;
    }

    public Double getTotalTTC() {
        return totalTTC;
    }

    public Double getTotalTVA() {
        return totalTVA;
    }

    public Double getTotalHT() {
        return totalHT;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public String getType() {
        return type;
    }

    public boolean isVerified() {
        return verified;
    }

    public int getYear() {
        return year;
    }

    public Double getTotalAmountPayment() {
        return totalAmountPayment;
    }

    public Set<DetailFactureClientDocument> getDetailsFacture() {
        return detailsFacture;
    }
}
