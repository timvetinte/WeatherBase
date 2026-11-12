package org.example.weatherbase.smhi;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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

    public List <SmhiObservation> fetchArchive(int parameter, long stationId){
        String csv;

        try {
            csv = client.get()
                    .uri("/parameter/{p}/station/{s}/period/corrected-archive/data.csv", parameter, stationId)
                    .retrieve().body(String.class);
        } catch (HttpClientErrorException e){
            return List.of();
        }


        //Måste göras klart
        return csv.lines().map(line -> line.split(";"))
                .filter(parts -> parts.length >= 5 && parts[2]
                        .matches("\\d{4}-\\d{2}-\\d{2}"))
                        .filter(parts -> !parts[3].isBlank())
                        .map(parts -> {
                            SmhiObservation o = new SmhiObservation();
                            o.setRef(parts[2]);
                            o.setValue(parts[3]);
                            o.setQuality(parts[4]);
                            return o;
                        })
                        .toList();
    }
    }






