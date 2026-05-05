package com.example.demo.dealership;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deals")
public class CarDealController {

    private final CarDealService service;

    public CarDealController(CarDealService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CarDeal> createDeal(@RequestBody CarDealRequest request) {
        CarDeal savedDeal = service.createDeal(request);
        return ResponseEntity
                .created(URI.create("/api/deals/" + savedDeal.getId()))
                .body(savedDeal);
    }

    @GetMapping
    public List<CarDeal> getAllDeals() {
        return service.getAllDeals();
    }

    @GetMapping("/search")
    public List<CarDeal> searchDealsByName(@RequestParam String name) {
        return service.searchDealsByName(name);
    }

    @PatchMapping("/{id}")
    public CarDeal patchDeal(@PathVariable Long id, @RequestBody CarDealPatchRequest request) {
        return service.patchDeal(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable Long id) {
        service.deleteDeal(id);
        return ResponseEntity.noContent().build();
    }
}
