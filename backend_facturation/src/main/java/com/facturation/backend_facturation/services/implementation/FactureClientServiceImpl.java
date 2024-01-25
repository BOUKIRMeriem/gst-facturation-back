package com.facturation.backend_facturation.services.implementation;


import com.facturation.backend_facturation.document.ClientDocument;
import com.facturation.backend_facturation.document.DetailFactureClientDocument;
import com.facturation.backend_facturation.document.FactureClientDocument;
import com.facturation.backend_facturation.exceptions.exception.ResourceNotFoundException;
import com.facturation.backend_facturation.repository.ClientRepository;
import com.facturation.backend_facturation.repository.DetailFactureClientRepository;
import com.facturation.backend_facturation.repository.FactureClientRepository;
import com.facturation.backend_facturation.services.FactureClientService;
import com.facturation.backend_facturation.utils.FrenchNumberToWords;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class FactureClientServiceImpl implements FactureClientService {

    @Autowired
    private FactureClientRepository factureClientRepository;

    @Autowired
    private DetailFactureClientRepository detailFactureClientRepository;

    @Override
    public List<FactureClientDocument> getAll(String clientId) {
        List<FactureClientDocument> list = new ArrayList<>();
        if(clientId != null && !"null".equalsIgnoreCase(clientId) && !"undefined".equalsIgnoreCase(clientId)){
            list = factureClientRepository.findAllByClient_Id(clientId);
        } else {
            list = factureClientRepository.findAll();
        }
        return list;
    }

    @Override
    public FactureClientDocument verifiedFacture(String factureId) {
        // find a facture by its id and throw an exception if the facture is not found
        FactureClientDocument factureClient = factureClientRepository.findById(factureId).orElseThrow(()->
                new ResourceNotFoundException("Facture not found with id {} " + factureId));
        factureClient.setVerified(true);
        return factureClientRepository.save(factureClient);
    }

    @Override
    public FactureClientDocument unVerifiedFacture(String factureId) {
        // find a facture by its id and throw an exception if the facture is not found
        FactureClientDocument factureClient = factureClientRepository.findById(factureId).orElseThrow(()->
                new ResourceNotFoundException("Facture not found with id {} " + factureId));
        factureClient.setVerified(false);
        return factureClientRepository.save(factureClient);
    }

    @Override
    public FactureClientDocument save(FactureClientDocument facture) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(facture.getDate());
        facture.setVerified(false).setYear(calendar.get(Calendar.YEAR));
        return factureClientRepository.save(facture);
    }

    @Override
    public FactureClientDocument getById(String factureId) {
        FactureClientDocument factureClient = factureClientRepository.findById(factureId).orElseThrow(()->
                new ResourceNotFoundException("Facture not found with id {} " + factureId));
        return factureClient;
    }

    @Override
    public FactureClientDocument update(FactureClientDocument facture, String factureId) {
        FactureClientDocument factureClient = factureClientRepository.findById(factureId).orElseThrow(()->
                new ResourceNotFoundException("Facture not found with id {} " + factureId));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(facture.getDate());
        detailFactureClientRepository.deleteAllByFactureClient(factureClient);
        factureClient.setDate(facture.getDate())
                .setTotalDiscount(facture.getTotalDiscount())
                .setVerified(false)
                .setTotalTVA(facture.getTotalTVA())
                .setTotalHT(facture.getTotalHT())
                .setClient(facture.getClient())
                .setTotalTTC(facture.getTotalTTC())
                .setType(facture.getType())
                .setYear(calendar.get(Calendar.YEAR))
                .setNumPiece(facture.getNumPiece())
                .setNumOrigin(facture.getNumOrigin())
                .setDetailsFacture(facture.getDetailsFacture());
        factureClientRepository.save(factureClient);
        return null;
    }

    @Override
    public FactureClientDocument delete(String factureId) {
        FactureClientDocument factureClient = factureClientRepository.findById(factureId).orElseThrow(()->
                new ResourceNotFoundException("Facture not found with id {} " + factureId));
        if(!factureClient.isVerified()){
            detailFactureClientRepository.deleteAllByFactureClient(factureClient);
            factureClientRepository.delete(factureClient);
        }
        return factureClient;
    }

    @Override
    public byte[] printFactureById(String id) throws JRException {
        // find a facture by its id and throw an exception if the facture is not found
        FactureClientDocument factureClient = factureClientRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Facture not found with id {} " + id));
        JasperPrint empReport = new JasperPrint();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols);
            ArrayList<Map<String, Object>> liste = new ArrayList<Map<String, Object>>();
            for (DetailFactureClientDocument detail : factureClient.getDetailsFacture()) {
                Map<String, Object> SC = new HashMap<>();
                SC.put("nameProduct", detail.getDescription() != null ? detail.getDescription() : detail.getProduct() != null ? detail.getProduct().getName().toUpperCase() : "");
                SC.put("reference", detail.getProduct() != null ? detail.getProduct().getReference().toUpperCase() : "");
                SC.put("price", detail.getPrice() != null ? formatter.format(detail.getPrice()) : "0,00");
                SC.put("qte", detail.getQuantity() != null ? formatter.format(detail.getQuantity()) : "0,00");
                SC.put("total", detail.getTotal() != null ? formatter.format(detail.getTotal()) : "0,00");
                SC.put("tva", detail.getTva() != null ? detail.getTva().intValue() + "%" : "");
                liste.add(SC);
            }
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("clientId", factureClient.getClient() != null ? factureClient.getClient().getCode().toUpperCase() : "");
            parameters.put("clientName", factureClient.getClient() != null ? factureClient.getClient().getName().toUpperCase(): "");
            parameters.put("clientCity", "Tanger");
            parameters.put("clientICE", factureClient.getClient().getIce() != null && factureClient.getClient().getIce() != " " ? "ICE : " + factureClient.getClient().getIce() : "");
            parameters.put("rcSite", 315478);
            parameters.put("lfSite", 254783300);
            parameters.put("rib", "RIB : 640 21 54565656565656565 00 56");
            parameters.put("faxSite", "05 39 35 65 95");
            parameters.put("cnssSite", 7510258);
            parameters.put("iceSite", "5665656");
            parameters.put("patente", 3214587);
            parameters.put("phoneSite", "06 61 54 78 98");
            parameters.put("numPiece", factureClient.getNumPiece());
            parameters.put("date", factureClient.getDate());
            parameters.put("nameSite", "RAN DISTRIBUTION S.A.R.L");
            parameters.put("addressSite", "Lotissement AMAL LOT N°120 TANGER");
            parameters.put("description2Site", "");
            parameters.put("descriptionSite", "VENTE & DISTRIBUTION DE MATERIAUX DE CONSTRUCTION.");
            parameters.put("totalTva", factureClient.getTotalTVA() != null ? formatter.format(factureClient.getTotalTVA()) : "0,00");
            parameters.put("totalHT", factureClient.getTotalHT() != null ? formatter.format(factureClient.getTotalHT()) : "0,00");
            parameters.put("totalTTC", factureClient.getTotalTTC() != null ? formatter.format(factureClient.getTotalTTC()) : "0,00");
            parameters.put("factureId", factureClient.getNumPiece() + "/" + factureClient.getYear());
            parameters.put("ttcInFrench", "*** " + FrenchNumberToWords.convert(factureClient.getTotalTTC()).toUpperCase() + " DIRHAMS ***");
            if ("HT".equalsIgnoreCase(factureClient.getType())){
                parameters.put("titleAmount", "Total H.T");
            } else if ("PTTC".equalsIgnoreCase(factureClient.getType())){
                parameters.put("titleAmount", "Total T.T.C");
            } else {
                parameters.put("titleAmount", "Total");
            }
            parameters.put("paymentDescription", "ESPECES");
            parameters.put("deadline", factureClient.getDate());
            Double netPaye = factureClient.getTotalTTC() + factureClient.getTotalTTC() * 0.0025;
            parameters.put("netPaye",  netPaye != null ? formatter.format(netPaye) : "0,00");
            parameters.put("amountTimber", factureClient.getTotalTTC() != null ? formatter.format(factureClient.getTotalTTC() * 0.0025) : "0,00");

            int n = 0;
            int numberRow = 24;
            for (int i = 0; i < numberRow - factureClient.getDetailsFacture().size(); i++) {
                Map<String, Object> SC = new HashMap<>();
                SC.put("nameProduct", "");
                SC.put("reference", "");
                SC.put("price", "");
                SC.put("qte", "");
                SC.put("total", "");
                SC.put("tva", "");
                liste.add(SC);
            }
            final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                    Collections.synchronizedList(liste));
            empReport =
                    JasperFillManager.fillReport
                            (
                                    JasperCompileManager.compileReport(
                                            ResourceUtils.getFile("classpath:jasperReport/facture.jrxml")
                                                    .getAbsolutePath()) // path of the jasper report
                                    , parameters // dynamic parameters
                                    , dataSource
                            );
        }catch (Exception ex){
            log.error("Error :: {} "+ex);
        }
        return JasperExportManager.exportReportToPdf(empReport);
    }
    @Override
    public long countFacture(){
        return factureClientRepository.count();
    }
    @Override
    public long countTotalTTC() {

        List<FactureClientDocument> factures = factureClientRepository.findAll();
        return (long) factures.stream().mapToDouble(FactureClientDocument::getTotalTTC).sum();
    }
}
