package com.seaport.ServiceTwo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seaport.ServiceTwo.common.Ship;
import com.seaport.ServiceTwo.service.JsonWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/serviceTwo")
public class ServiceTwoController {
    public ServiceTwoInterface serviceTwoInterface;

    @Autowired
    public ServiceTwoController(ServiceTwoInterface serviceTwoInterface) {
        this.serviceTwoInterface = serviceTwoInterface;
    }

    @GetMapping("/getJson/{amount}")
    public String getShipJson(String ships, @PathVariable("amount") int amount) {
        String url = "http://localhost:8081/serviceOne/" + amount;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        serviceTwoInterface.getShipsJson(responseEntity.getBody(), amount);

        return "Json with " + amount + " ships was created.";
    }

    @GetMapping("/getFromJson/{json}")
    public String getShipsFromJson(@PathVariable("json") String json) {
        List<Ship> list = null;
        try {
            list = serviceTwoInterface.getShipsFromJson(json);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String ships = gson.toJson(list);
            return ships;
        } catch (FileNotFoundException e) {
            return "File was not found.";
        }
    }

    @GetMapping("/addShipToJson/{json}")
    public String addShipToJson(@PathVariable("json") String json) {
        List<Ship> list = null;
        try {
            list = serviceTwoInterface.getShipsFromJson(json);
            list = serviceTwoInterface.addShipFromConsoleToJson(list);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String ships = gson.toJson(list);
            JsonWriter.writeToJson(ships);
             return "Ship(s) were added";
        } catch (FileNotFoundException e) {
            return "File was not found.";
        }
    }

    @PostMapping("/postReport")
    public String postReport(@RequestBody String string) {
        return serviceTwoInterface.postReport(string);
    }
}
