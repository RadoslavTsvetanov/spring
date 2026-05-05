package com.example.demo.dealership;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarDealServiceTest {

    @Mock
    private CarDealRepository repository;

    @InjectMocks
    private CarDealService service;

    @Test
    void shouldCreateDeal() {
        CarDealRequest request = new CarDealRequest(
                "Family SUV Discount",
                "Toyota",
                "RAV4",
                2023,
                31999.99,
                "Low mileage family car"
        );
        CarDeal savedDeal = buildDeal(1L, "Family SUV Discount", "Toyota", "RAV4", 2023, 31999.99,
                "Low mileage family car");
        when(repository.save(any(CarDeal.class))).thenReturn(savedDeal);

        CarDeal result = service.createDeal(request);

        assertEquals(1L, result.getId());
        assertEquals("Family SUV Discount", result.getName());
        verify(repository).save(any(CarDeal.class));
    }

    @Test
    void shouldReturnAllDeals() {
        List<CarDeal> deals = List.of(
                buildDeal(1L, "Family SUV Discount", "Toyota", "RAV4", 2023, 31999.99, "SUV"),
                buildDeal(2L, "Sport Coupe Deal", "BMW", "M4", 2022, 58999.50, "Coupe")
        );
        when(repository.findAll()).thenReturn(deals);

        List<CarDeal> result = service.getAllDeals();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void shouldSearchDealsByName() {
        List<CarDeal> deals = List.of(
                buildDeal(1L, "Family SUV Discount", "Toyota", "RAV4", 2023, 31999.99, "SUV")
        );
        when(repository.findByNameContainingIgnoreCase("SUV")).thenReturn(deals);

        List<CarDeal> result = service.searchDealsByName("SUV");

        assertEquals(1, result.size());
        assertEquals("Family SUV Discount", result.getFirst().getName());
        verify(repository).findByNameContainingIgnoreCase("SUV");
    }

    @Test
    void shouldPatchDeal() {
        CarDeal existingDeal = buildDeal(1L, "Family SUV Discount", "Toyota", "RAV4", 2023, 31999.99,
                "Low mileage family car");
        when(repository.findById(1L)).thenReturn(Optional.of(existingDeal));
        when(repository.save(existingDeal)).thenReturn(existingDeal);

        CarDeal result = service.patchDeal(1L, new CarDealPatchRequest(
                "Updated SUV Discount",
                null,
                null,
                null,
                29999.99,
                "Updated dealership offer"
        ));

        assertEquals("Updated SUV Discount", result.getName());
        assertEquals(29999.99, result.getPrice());
        assertEquals("Updated dealership offer", result.getDescription());
        verify(repository).save(existingDeal);
    }

    @Test
    void shouldThrowWhenPatchingMissingDeal() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.patchDeal(99L, new CarDealPatchRequest(null, null, null, null, 10.0, null)));
        verify(repository, never()).save(any(CarDeal.class));
    }

    @Test
    void shouldDeleteDeal() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteDeal(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingMissingDeal() {
        when(repository.existsById(55L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> service.deleteDeal(55L));
        verify(repository, never()).deleteById(55L);
    }

    private CarDeal buildDeal(
            Long id,
            String name,
            String brand,
            String model,
            Integer productionYear,
            Double price,
            String description
    ) {
        CarDeal deal = new CarDeal();
        deal.setId(id);
        deal.setName(name);
        deal.setBrand(brand);
        deal.setModel(model);
        deal.setProductionYear(productionYear);
        deal.setPrice(price);
        deal.setDescription(description);
        return deal;
    }
}
