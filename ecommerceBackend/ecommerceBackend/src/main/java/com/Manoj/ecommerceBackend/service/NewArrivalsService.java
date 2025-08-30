package com.Manoj.ecommerceBackend.service;

import com.Manoj.ecommerceBackend.Entity.NewArrival;
import com.Manoj.ecommerceBackend.Entity.Product;
import com.Manoj.ecommerceBackend.dto.NewArrivalRequest;
import com.Manoj.ecommerceBackend.repo.NewArrivalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewArrivalsService {
    @Autowired
    private NewArrivalsRepository newArrivalsRepository;

    public List<NewArrival> getNewArrivals(){
        return newArrivalsRepository.findAll();
    }

    public NewArrival addNewArrival(NewArrivalRequest request){
        Optional<NewArrival> isExist = newArrivalsRepository.findByName(request.getName());
        if(isExist.isPresent()){
            throw new RuntimeException("Product Already Exists");
        }

        NewArrival newArrival = NewArrival.builder()
                .name(request.getName())
                .image(request.getImage())
                .price(request.getPrice())
                .build();

        return newArrivalsRepository.save(newArrival);
    }

    public NewArrival getNewArrivalById(Integer id){
        return newArrivalsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id" + id));
    }
}
