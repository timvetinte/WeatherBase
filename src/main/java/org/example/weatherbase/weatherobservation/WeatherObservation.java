package org.example.weatherbase.weatherobservation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.weatherbase.station.Station;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"station_id", "date"}))
@Getter
@Setter
public class WeatherObservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (optional = false)
    private Station station;

    private LocalDate date;
    private BigDecimal valueMm;
    private String quality;

    public String getAllInfo(){
        return station.getName() + " " + date + ": " + valueMm + " mm. Quality: " + quality;
    };

}
