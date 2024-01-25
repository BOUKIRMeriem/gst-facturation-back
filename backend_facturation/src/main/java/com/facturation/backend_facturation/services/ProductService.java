package com.facturation.backend_facturation.services;

import com.facturation.backend_facturation.document.ProductDocument;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<ProductDocument> getAllProduct();
    ProductDocument saveProduct(ProductDocument product);
    ProductDocument updateProduct(ProductDocument product, String idProduct);
    ProductDocument getProductById(String idProduct);
    ProductDocument deleteProductById(String idProduct);
    long countProduct();
}
