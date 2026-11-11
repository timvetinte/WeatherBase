package org.example.weatherbase.station;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Station {

    @Id
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private boolean active;


}
