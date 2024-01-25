package com.facturation.backend_facturation.services.implementation;


import com.facturation.backend_facturation.document.ProductDocument;
import com.facturation.backend_facturation.repository.ProductRepository;
import com.facturation.backend_facturation.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDocument> getAllProduct() {
        return productRepository.findAll();
    }
    @Override
    public ProductDocument saveProduct(ProductDocument product) {
        System.out.println("Saving new product " + product.getReference() + " to the database!!");
        ProductDocument savedProduct = productRepository.save(product);

        return savedProduct;
    }
    @Override
    public ProductDocument updateProduct(ProductDocument product, String productId) {
        ProductDocument product1 = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
        product1.setReference(product.getReference());
        product1.setReference(product.getReference());
        product1.setName(product.getName());
        product1.setPurchasePrice(product.getPurchasePrice());
        product1.setSellingPrice(product.getSellingPrice());
        product1.setDepartureQuantity(product.getDepartureQuantity());
        product1.setTotalQuantity(product.getTotalQuantity());
        return productRepository.save(product1);
    }
    @Override
    public ProductDocument getProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
    }


    @Override
    public ProductDocument deleteProductById(String productId) {
        ProductDocument product = productRepository.findById(productId).orElseThrow(()->
                new com.facturation.backend_facturation.exceptions.exception.ResourceNotFoundException("product not found with id {} "+productId));
        productRepository.delete(product);
        return product;
    }
    @Override
    public long countProduct(){
        return productRepository.count();
    }

}
