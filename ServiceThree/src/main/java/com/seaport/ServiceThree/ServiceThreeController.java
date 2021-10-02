package com.seaport.ServiceThree;

import com.google.gson.reflect.TypeToken;
import com.seaport.ServiceThree.common.Ship;
import com.seaport.ServiceThree.common.TypeOfCargo;
import com.seaport.ServiceThree.service.Unloading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/serviceThree")
public class ServiceThreeController {
    public ServiceThreeInterface serviceThreeInterface;

    @Autowired
    public ServiceThreeController(ServiceThreeInterface serviceThreeInterface) {
        this.serviceThreeInterface = serviceThreeInterface;
    }

    @GetMapping("/report/{jsonName}")
    public String getReport(@PathVariable("jsonName") String jsonName) {
        List<Ship> schedule;

        try {
            schedule = serviceThreeInterface.getSchedule(jsonName);
        } catch (FileNotFoundException e) {
            return e.getMessage();
        }

        String string = "";

        for (TypeOfCargo type: TypeOfCargo.values()) {
            List<Ship> list = new ArrayList<>();
            for (Ship ship: schedule) {
                if (ship.getTypeOfCargo() == type) {
                    list.add(ship);
                }
            }
            Unloading unloading = new Unloading(list);
            string += unloading.startUnload();
        }
        string += "End Of Report";
        return string;
    }
}
