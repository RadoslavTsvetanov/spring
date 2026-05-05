package com.example.demo.dealership;

public record CarDealRequest(
        String name,
        String brand,
        String model,
        Integer productionYear,
        Double price,
        String description
) {
}
