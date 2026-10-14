package org.example.weatherbase.smhi;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SmhiStation {


    private Long id;
    private String name;
    private Long latitude;
    private Long longitude;
    private boolean active;


}
