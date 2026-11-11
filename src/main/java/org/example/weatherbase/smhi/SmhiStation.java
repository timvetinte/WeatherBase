package org.example.weatherbase.smhi;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SmhiStation {


    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private boolean active;


}
