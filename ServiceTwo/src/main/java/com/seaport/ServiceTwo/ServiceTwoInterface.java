package com.seaport.ServiceTwo;

import com.seaport.ServiceTwo.common.Ship;

import java.io.FileNotFoundException;
import java.util.List;

public interface ServiceTwoInterface {
    void getShipsJson(String ships, int amount);
    List<Ship> getShipsFromJson(String json) throws FileNotFoundException;
    List<Ship> addShipFromConsoleToJson(List<Ship> schedule) throws FileNotFoundException;
    String postReport(String jsonName);
}
