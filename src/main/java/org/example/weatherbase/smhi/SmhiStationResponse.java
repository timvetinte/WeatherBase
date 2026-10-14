package org.example.weatherbase.smhi;

import lombok.Data;

import java.util.List;

@Data
public class SmhiStationResponse {

    private List <SmhiStation> station;
}
