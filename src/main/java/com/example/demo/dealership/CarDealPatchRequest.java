package com.example.demo.dealership;

public record CarDealPatchRequest(
        String name,
        String brand,
        String model,
        Integer productionYear,
        Double price,
        String description
) {
}
