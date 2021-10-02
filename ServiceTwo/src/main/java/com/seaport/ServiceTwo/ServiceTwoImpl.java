package com.seaport.ServiceTwo;

import com.seaport.ServiceTwo.common.Ship;
import com.seaport.ServiceTwo.common.TypeOfCargo;
import com.seaport.ServiceTwo.service.JsonReader;
import com.seaport.ServiceTwo.service.JsonWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ServiceTwoImpl implements ServiceTwoInterface {
    @Override
    public void getShipsJson(String ships, int amount) {
        JsonWriter.writeToJson(ships);
    }

    @Override
    public List<Ship> getShipsFromJson(String json) throws FileNotFoundException {
        return JsonReader.readFromJson(json);
    }

    @Override
    public List<Ship> addShipFromConsoleToJson(List<Ship> schedule) throws FileNotFoundException {
        try {
            return addShipFromConsole(schedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    @Override
    public String postReport(String jsonName) {
        String url = "http://localhost:8083/serviceThree/report/" + jsonName;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        if (responseEntity.getBody().equals("File was not found.")) {
            return "File was not found";
        }
        try (FileWriter file = new FileWriter("report.json")) {
            file.write(responseEntity.getBody());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Report was written to json file.";
    }

    List<Ship> addShipFromConsole(List<Ship> schedule) throws ParseException, IllegalArgumentException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to add another ship by yourself?\nYes/No");
        String answer = scanner.nextLine();

        while(answer.equals("Yes")) {
            Ship ship = new Ship();

            System.out.println("Type the name of the ship: ");
            ship.setNameOfShip(scanner.nextLine());

            System.out.println("Type the type of cargo(Bulker, Container, Tanker): ");
            ship.setTypeOfCargo(TypeOfCargo.valueOf(scanner.nextLine().toUpperCase(Locale.ROOT)));

            System.out.println("Type weight of the cargo: ");
            ship.setWeight(Integer.parseInt(scanner.nextLine()));

            System.out.println("Type the day of arrival (1-30): ");
            String dateString = scanner.nextLine() + "-4-2021 ";

            System.out.println("Type time of arrival (hour:minutes:seconds): ");
            dateString += scanner.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            Date date = sdf.parse(dateString);

            Calendar timeOfArrival = Calendar.getInstance();
            timeOfArrival.setTime(date);
            if (timeOfArrival.get(Calendar.MONTH) != Calendar.APRIL || timeOfArrival.get(Calendar.DAY_OF_MONTH) == 31) {
                throw new IllegalArgumentException("Wrong date.");
            }
            ship.setScheduledTimeOfArrival(timeOfArrival);

            Random random = new Random();
            timeOfArrival.add(Calendar.DAY_OF_MONTH, random.nextInt(14) - 7);
            if (timeOfArrival.get(Calendar.MONTH) != Calendar.APRIL) {
                throw new IllegalArgumentException("Date of actual time of arrival is out of bounds.");
            }
            ship.setTimeOfArrival(timeOfArrival);
            int timeOfUnload = (int) (ship.getWeight() / ship.getTypeOfCargo().getProductivityOfCrane());
            int delayOfUnload = random.nextInt(1440);
            ship.setTimeOfUnload(timeOfUnload + delayOfUnload);
            ship.setDelayOfUnload(delayOfUnload);
            schedule.add(ship);
            System.out.println("Ship was added successfully");
            System.out.println("Do you want to add another one?\nYes/No");
            answer = scanner.nextLine();
        }
        Collections.sort(schedule);
        return schedule;
    }
}
