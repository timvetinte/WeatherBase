package org.example.weatherbase.smhi;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class SmhiClient {

    private final RestClient client = RestClient.builder()
            .baseUrl("https://opendata-download-metobs.smhi.se/api/version/1.0")
            .build();


    public List<SmhiStation> fetchStations(int paramater) {
        SmhiStationResponse response = client.get()
                .uri("/parameter/{p}.json", paramater)
                .retrieve().body(SmhiStationResponse.class);
        return response.getStation();
    }

    public List<SmhiObservation> fetchLatestMonths(int parameter, long stationId){
        SmhiObservationResponse response = client.get()
                .uri("/parameter/{p}/station/{s}/period/latest-months/data.json", parameter, stationId)
                .retrieve().body(SmhiObservationResponse.class);
        return response.getValue();
    }
    }


