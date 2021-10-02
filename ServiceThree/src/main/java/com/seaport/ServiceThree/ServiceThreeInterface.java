package com.seaport.ServiceThree;

import com.seaport.ServiceThree.common.Ship;

import java.io.FileNotFoundException;
import java.util.List;

public interface ServiceThreeInterface {
    List<Ship> getSchedule(String jsonName) throws FileNotFoundException;
}
