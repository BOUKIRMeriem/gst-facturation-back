package com.facturation.backend_facturation.controller;

import com.facturation.backend_facturation.document.ProductDocument;
import com.facturation.backend_facturation.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequestMapping("/api/product/")
@RestController
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDocument>> getAllProduct(){
        log.info("Request for getting all the products");
        List<ProductDocument> productList = productService.getAllProduct();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDocument> getProductById(@PathVariable("productId") String productId) {
        try {
            log.info("Request for getting product");
            ProductDocument product = productService.getProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found", ex);
        }
    }
    @PostMapping
    public ResponseEntity<ProductDocument> createProduct(@RequestBody ProductDocument product) {
        try {
            log.info("Request for adding new product");
            ProductDocument savedProduct = productService.saveProduct(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Error adding new product: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add new product", ex);
        }
    }
    @PutMapping("{productId}")
    public ResponseEntity<ProductDocument> updateProduct(@PathVariable("productId") String productId, @RequestBody ProductDocument product) {
        try {
            log.info("Request for updating product");
            ProductDocument updatedProduct = productService.updateProduct(product, productId);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error updating product: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found", ex);
        }
    }


    @DeleteMapping("delete/{productId}")
    public ResponseEntity<ProductDocument> deleteProduct(@PathVariable("productId") String productId){
        try {
            log.info("Request for deleting product");
            ProductDocument product = productService.deleteProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error deleting product: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found", ex);
        }
    }
    @GetMapping("count")
    public long countProduct(){
        return productService.countProduct();

    }

}
