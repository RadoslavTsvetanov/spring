package com.example.demo.dealership;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarDealService {

    private final CarDealRepository repository;

    public CarDealService(CarDealRepository repository) {
        this.repository = repository;
    }

    public CarDeal createDeal(CarDealRequest request) {
        CarDeal deal = new CarDeal();
        deal.setName(request.name());
        deal.setBrand(request.brand());
        deal.setModel(request.model());
        deal.setProductionYear(request.productionYear());
        deal.setPrice(request.price());
        deal.setDescription(request.description());
        return repository.save(deal);
    }

    public List<CarDeal> getAllDeals() {
        return repository.findAll();
    }

    public List<CarDeal> searchDealsByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public CarDeal patchDeal(Long id, CarDealPatchRequest request) {
        CarDeal deal = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Deal not found"));

        if (request.name() != null) {
            deal.setName(request.name());
        }
        if (request.brand() != null) {
            deal.setBrand(request.brand());
        }
        if (request.model() != null) {
            deal.setModel(request.model());
        }
        if (request.productionYear() != null) {
            deal.setProductionYear(request.productionYear());
        }
        if (request.price() != null) {
            deal.setPrice(request.price());
        }
        if (request.description() != null) {
            deal.setDescription(request.description());
        }

        return repository.save(deal);
    }

    public void deleteDeal(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deal not found");
        }
        repository.deleteById(id);
    }
}
