curl -X POST http://localhost:8082/api/deals -H "Content-Type: application/json" -d "{\"name\":\"Family SUV Discount\",\"brand\":\"Toyota\",\"model\":\"RAV4\",\"productionYear\":2023,\"price\":31999.99,\"description\":\"Low mileage family car\"}"
curl http://localhost:8082/api/deals
curl "http://localhost:8082/api/deals/search?name=SUV"
curl -X PATCH http://localhost:8082/api/deals/1 -H "Content-Type: application/json" -d "{\"price\":29999.99,\"description\":\"Updated dealership offer\"}"
curl -X DELETE http://localhost:8082/api/deals/1
