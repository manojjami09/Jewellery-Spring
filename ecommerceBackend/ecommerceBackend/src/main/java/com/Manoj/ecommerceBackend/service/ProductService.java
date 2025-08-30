package com.Manoj.ecommerceBackend.service;

import com.Manoj.ecommerceBackend.Entity.NewArrival;
import com.Manoj.ecommerceBackend.Entity.Product;
import com.Manoj.ecommerceBackend.dto.ProductRequest;
import com.Manoj.ecommerceBackend.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(ProductRequest request){
        Optional<Product> isExist = productRepository.findByName(request.getName());
        if(isExist.isPresent()){
            throw new RuntimeException("Product Already Exists");
        }

        Product product = Product.builder()
                .name(request.getName())
                .image(request.getImage())
                .price(request.getPrice())
                .build();

        return productRepository.save(product);
    }

    public Product getProductById(Integer id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id" + id));
    }


}
