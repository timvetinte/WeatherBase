package org.example.weatherbase.smhi;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SmhiObservation {


    private String ref;
    private String value;
    private String quality;
}
