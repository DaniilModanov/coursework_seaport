package com.seaport.ServiceThree.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seaport.ServiceThree.common.Ship;
import com.seaport.ServiceThree.common.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Unloading {
    private int amountOfShips;
    private int cranesQuantity = 0;
    private int fine = 0;

    private List<Ship> ships;

    public Unloading(List<Ship> ships) {
        this.ships = ships;
        amountOfShips = ships.size();
    }

    public String startUnload() {
        while (fine >= Utilities.PRICE_OF_CRANE * cranesQuantity) {
            ConcurrentLinkedQueue<Ship> queueOfShips = new ConcurrentLinkedQueue<>(ships);
            fine = 0;
            cranesQuantity++;
            List<UnloadingCrane> cranes = new ArrayList<>(cranesQuantity);

            for (int i = 0; i < cranesQuantity; i++) {
                UnloadingCrane crane = new UnloadingCrane(queueOfShips);
                cranes.add(crane);
            }

            ExecutorService executorService = Executors.newFixedThreadPool(cranesQuantity);
            try {
                List<Future<Integer>> futures = executorService.invokeAll(cranes);
                fine = futures.stream().mapToInt(a -> {
                   try {
                       return a.get();
                   } catch (InterruptedException | ExecutionException e) {
                       e.printStackTrace();
                    }
                   return 0;
                }).sum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
        }
        for (Ship ship : ships) {
            int tempTime = (int) (ship.getTimeOfUnloadStart().getTimeInMillis() -
                    ship.getTimeOfArrival().getTimeInMillis()) / 1000 / 60;
            ship.setTimeOfWait(tempTime);
        }

        String string = ships.get(0).getTypeOfCargo().toString() +
                " Fine: " + fine +
                " Amount of Cranes: " + cranesQuantity +
                " Amount of ships: " + amountOfShips +
                " Max time of wait: " +
                Utilities.intToDateFormat(ships.stream().mapToInt(Ship::getTimeOfWait).max().orElse(0)) +
                " Average time of wait: " +
                Utilities.intToDateFormat(ships.stream().mapToInt(Ship::getTimeOfWait).sum() / amountOfShips) +
                " Max time of delay unload: " +
                Utilities.intToDateFormat(ships.stream().mapToInt(Ship::getDelayOfUnload).max().orElse(0)) +
                " Average time of delay of unload: " +
                Utilities.intToDateFormat(ships.stream().mapToInt(Ship::getDelayOfUnload).sum() / amountOfShips);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(ships) + gson.toJson(string) + "\n";
    }
}
