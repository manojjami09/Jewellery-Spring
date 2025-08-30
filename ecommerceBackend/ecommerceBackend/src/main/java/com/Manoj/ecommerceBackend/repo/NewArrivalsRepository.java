package com.Manoj.ecommerceBackend.repo;

import com.Manoj.ecommerceBackend.Entity.NewArrival;
import com.Manoj.ecommerceBackend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewArrivalsRepository extends JpaRepository<NewArrival, Integer> {
    Optional<NewArrival> findByName(String name);
}
