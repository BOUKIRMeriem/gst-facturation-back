package com.facturation.backend_facturation.services;

import com.facturation.backend_facturation.document.FactureClientDocument;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FactureClientService {

    List<FactureClientDocument> getAll(String clientId);

    FactureClientDocument verifiedFacture(String factureId);

    FactureClientDocument unVerifiedFacture(String factureId);

    FactureClientDocument save(FactureClientDocument facture);

    FactureClientDocument getById(String factureId);

    FactureClientDocument update(FactureClientDocument facture, String factureId);

    FactureClientDocument delete(String factureId);

    byte[] printFactureById(String id) throws JRException;
    long countFacture();
    long countTotalTTC();
}
