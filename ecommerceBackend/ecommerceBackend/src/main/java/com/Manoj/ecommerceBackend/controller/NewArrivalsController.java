package com.Manoj.ecommerceBackend.controller;

import com.Manoj.ecommerceBackend.Entity.NewArrival;
import com.Manoj.ecommerceBackend.Entity.Product;
import com.Manoj.ecommerceBackend.dto.NewArrivalRequest;
import com.Manoj.ecommerceBackend.dto.ProductRequest;
import com.Manoj.ecommerceBackend.service.NewArrivalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/newArrivals")
@CrossOrigin(origins = "http://localhost:5173")
public class NewArrivalsController {

    @Autowired
    private NewArrivalsService newArrivalsService;

    @GetMapping
    public ResponseEntity<List<NewArrival>> getNewArrivals(){
        return new ResponseEntity<>(newArrivalsService.getNewArrivals(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NewArrival> addNewArrival(@RequestBody NewArrivalRequest request){
        return new ResponseEntity<>(newArrivalsService.addNewArrival(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewArrival> getNewArrivalById(@PathVariable Integer id) {
        return new ResponseEntity<>(newArrivalsService.getNewArrivalById(id), HttpStatus.OK);
    }



}
