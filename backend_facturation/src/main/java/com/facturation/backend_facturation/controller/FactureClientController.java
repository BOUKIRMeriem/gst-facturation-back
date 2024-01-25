package com.facturation.backend_facturation.controller;

import com.facturation.backend_facturation.document.FactureClientDocument;
import com.facturation.backend_facturation.exceptions.exception.ResourceNotFoundException;
import com.facturation.backend_facturation.services.FactureClientService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@RequestMapping("/api/facture-client/")
@RestController
@CrossOrigin("*")
public class FactureClientController {

    @Autowired
    private FactureClientService factureClientService;

    @GetMapping("list/{clientId}")
    public ResponseEntity<List<FactureClientDocument>> getAll( @PathVariable("clientId") String clientId){
        try{
            log.info("Request for getting all facture");
            return new ResponseEntity<>(factureClientService.getAll(clientId), HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping
    public ResponseEntity<FactureClientDocument> create(@RequestBody FactureClientDocument facture){
        try {
            log.info("Request for adding new facture");
            return new ResponseEntity<>(factureClientService.save(facture), HttpStatus.CREATED);
        }catch (Exception ex){
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PutMapping("{factureId}")
    public ResponseEntity<FactureClientDocument> updateFacture(@RequestBody FactureClientDocument facture, @PathVariable("factureId") String factureId){
        try {
            log.info("Request for updating facture {} " + facture.getNumPiece());
            // update facture and return an CREATED response with the new facture information
            return new ResponseEntity<>(factureClientService.update(facture,factureId),HttpStatus.CREATED);
        }catch (ResourceNotFoundException ex){
            // log an error message if the facture not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a NOT_FOUND status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("{factureId}")
    public ResponseEntity<FactureClientDocument> deleteFacture(@PathVariable("factureId") String factureId){
        try {
            log.info("Request for deleting facture {} "+factureId);
            // delete facture and return an ok response with the facture information
            return new ResponseEntity<>(factureClientService.delete(factureId),HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            // log an error message if the facture is not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a NOT_FOUND status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("verified/{factureId}")
    public ResponseEntity<FactureClientDocument> verified(@PathVariable("factureId") String factureId) {
        log.info("Request for verified facture {} "+factureId);
        return new ResponseEntity<>(factureClientService.verifiedFacture(factureId),HttpStatus.OK);
    }

    @GetMapping("unverified/{factureId}")
    public ResponseEntity<FactureClientDocument> unVerified(@PathVariable("factureId") String factureId) {
        log.info("Request for unverified facture {} "+factureId);
        return new ResponseEntity<>(factureClientService.unVerifiedFacture(factureId),HttpStatus.OK);
    }

    @GetMapping("{factureId}")
    public ResponseEntity<FactureClientDocument> getFactureById(@PathVariable("factureId") String factureId){
        try {
            log.info("Request for getting facture");
            // find the facture by its id and return an OK response with the found facture information
            return new ResponseEntity<>(factureClientService.getById(factureId),HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            // log an error message if the facture is not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a NOT_FOUND status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("print/{id}")
    public ResponseEntity<byte[]> getReport(@PathVariable("id") String id) throws FileNotFoundException, JRException, ResourceNotFoundException {
        try{
            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "facture.pdf");
            return new ResponseEntity<byte[]>(factureClientService.printFactureById(id), headers, HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            // log an error message if the facture is not found
            log.error(ex.getMessage());
            // throw a ResponseStatusException with a NOT_FOUND status code and the exception message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
    @GetMapping("count")
    public long countFacture(){
        return factureClientService.countFacture();

    }
    @GetMapping("totalTTC")
    public long countTotalTTC(){
        return  factureClientService.countTotalTTC();
    }
}
