package com.facturation.backend_facturation.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="d_payment_client")
public class PaymentClientDocument {

    @Id
    private String id;

    private Date date;

    private String designation;

    private String tier;

    private String bank;

    private Date deadline;

    private Double amount;

    private int year;

    private ClientDocument client;

    private FactureClientDocument factureClient;

    private boolean verified = false;

    private String type;

    public FactureClientDocument getFactureClient() {
        return factureClient;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getDesignation() {
        return designation;
    }

    public String getTier() {
        return tier;
    }

    public String getBank() {
        return bank;
    }

    public Date getDeadline() {
        return deadline;
    }

    public Double getAmount() {
        return amount;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getType() {
        return type;
    }

    public ClientDocument getClient() {
        return client;
    }

    public int getYear() {
        return year;
    }
}
