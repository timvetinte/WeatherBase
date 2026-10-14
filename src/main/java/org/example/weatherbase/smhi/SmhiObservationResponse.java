package org.example.weatherbase.smhi;


import lombok.Data;

import java.util.List;

@Data
public class SmhiObservationResponse {

    private List<SmhiObservation> value;
}
