package com.seaport.ServiceOne.service.server;

import com.seaport.ServiceOne.common.Ship;
import com.seaport.ServiceOne.service.ScheduleGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceOneImpl implements ServiceOneInterface {
    @Override
    public List<Ship> getSchedule(int amount) {
        return new ScheduleGenerator(amount).getSchedule();
    }
}
