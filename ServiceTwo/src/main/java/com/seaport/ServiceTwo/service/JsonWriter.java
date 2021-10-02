package com.seaport.ServiceTwo.service;

import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter {
    public static void writeToJson(String ships) {
        try (FileWriter file = new FileWriter("ships.json")) {
            file.write(ships);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
