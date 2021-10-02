package com.seaport.ServiceOne.service.server;

import com.seaport.ServiceOne.common.Ship;

import java.util.List;

public interface ServiceOneInterface {
    List<Ship> getSchedule(int amount);
}
