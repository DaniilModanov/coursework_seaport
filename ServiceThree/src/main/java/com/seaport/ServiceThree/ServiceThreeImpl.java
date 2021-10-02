package com.seaport.ServiceThree;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seaport.ServiceThree.common.Ship;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class ServiceThreeImpl implements ServiceThreeInterface{

    @Override
    public List<Ship> getSchedule(String jsonName) throws FileNotFoundException {
        String url = "http://localhost:8082/serviceTwo/getFromJson/" + jsonName;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        try {
            Type itemsListType = new TypeToken<List<Ship>>() {}.getType();
            Gson gson = new Gson();
            return gson.fromJson(responseEntity.getBody(), itemsListType);

        } catch (Exception e) {
            throw new FileNotFoundException("File was not found");
        }
    }
}

