package org.example.weatherbase.monthlyprecipitation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"station_id", "year", "month"}))
@Getter
@Setter
@NoArgsConstructor
public class MonthlyPrecipitation {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private Long stationId;
    private Integer year;
    private Integer month;

    private BigDecimal totalMm;
    private Integer rainyDays;
    private Integer days;
}
