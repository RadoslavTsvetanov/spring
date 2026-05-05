package com.example.demo.dealership;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarDealRepository extends JpaRepository<CarDeal, Long> {

    List<CarDeal> findByNameContainingIgnoreCase(String name);
}
