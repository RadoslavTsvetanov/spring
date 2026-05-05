package com.example.demo.dealership;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarDealControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CarDealRepository repository;

    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    void shouldCreateListSearchPatchAndDeleteDeal() throws Exception {
        HttpResponse<String> createResponse = sendRequest("POST", "/api/deals", """
                {
                  "name": "Family SUV Discount",
                  "brand": "Toyota",
                  "model": "RAV4",
                  "productionYear": 2023,
                  "price": 31999.99,
                  "description": "Low mileage family car"
                }
                """);

        assertEquals(201, createResponse.statusCode());
        assertEquals("/api/deals/1", createResponse.headers().firstValue("Location").orElseThrow());
        assertTrue(createResponse.body().contains("\"name\":\"Family SUV Discount\""));
        assertTrue(createResponse.body().contains("\"brand\":\"Toyota\""));
        assertEquals(1, repository.count());

        HttpResponse<String> getAllResponse = sendRequest("GET", "/api/deals", null);
        assertEquals(200, getAllResponse.statusCode());
        assertTrue(getAllResponse.body().contains("\"model\":\"RAV4\""));

        HttpResponse<String> searchResponse = sendRequest("GET", "/api/deals/search?name=SUV", null);
        assertEquals(200, searchResponse.statusCode());
        assertTrue(searchResponse.body().contains("\"name\":\"Family SUV Discount\""));

        HttpResponse<String> patchResponse = sendRequest("PATCH", "/api/deals/1", """
                {
                  "price": 29999.99,
                  "description": "Updated dealership offer"
                }
                """);
        assertEquals(200, patchResponse.statusCode());
        assertTrue(patchResponse.body().contains("\"price\":29999.99"));
        assertTrue(patchResponse.body().contains("\"description\":\"Updated dealership offer\""));
        CarDeal updatedDeal = repository.findById(1L).orElseThrow();
        assertEquals(29999.99, updatedDeal.getPrice());
        assertEquals("Updated dealership offer", updatedDeal.getDescription());

        HttpResponse<String> deleteResponse = sendRequest("DELETE", "/api/deals/1", null);
        assertEquals(204, deleteResponse.statusCode());
        assertFalse(repository.existsById(1L));

        HttpResponse<String> getAfterDeleteResponse = sendRequest("GET", "/api/deals", null);
        assertEquals(200, getAfterDeleteResponse.statusCode());
        assertEquals("[]", getAfterDeleteResponse.body());
    }

    private HttpResponse<String> sendRequest(String method, String path, String body)
            throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpRequest request = switch (method) {
            case "POST" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body)).build();
            case "PATCH" -> requestBuilder.method("PATCH", HttpRequest.BodyPublishers.ofString(body)).build();
            case "DELETE" -> requestBuilder.DELETE().build();
            case "GET" -> requestBuilder.GET().build();
            default -> throw new IllegalArgumentException("Unsupported method: " + method);
        };

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
