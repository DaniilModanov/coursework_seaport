package com.seaport.ServiceOne.service.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seaport.ServiceOne.common.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/serviceOne")
public class ServiceOneController {
    private ServiceOneInterface serviceOneInterface;

    @Autowired
    public void setServiceOneInterface(ServiceOneInterface serviceOneInterface) {
        this.serviceOneInterface = serviceOneInterface;
    }

    @GetMapping("")
    public String getSchedule() {
        List<Ship> shipList = serviceOneInterface.getSchedule(100);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(shipList);
    }

    @GetMapping("/{amount}")
    public String getScheduleWithAmount(@PathVariable("amount") int amount) {
        List<Ship> shipList = serviceOneInterface.getSchedule(amount);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(shipList);
    }
}
