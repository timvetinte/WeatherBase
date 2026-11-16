package org.example.weatherbase.monthlyprecipitation;

import lombok.RequiredArgsConstructor;
import org.example.weatherbase.station.Station;
import org.example.weatherbase.station.StationRepository;
import org.example.weatherbase.weatherobservation.WeatherObservation;
import org.example.weatherbase.weatherobservation.WeatherObservationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonthlyPrecipitationService {

    private final WeatherObservationRepository observationRepository;

    private final JdbcTemplate jdbcTemplate;

    public void buildAll() {
        jdbcTemplate.update("TRUNCATE TABLE monthly_precipitation");

        jdbcTemplate.update("""
INSERT INTO monthly_precipitation (station_id, year, month, total_mm, rainy_days, days)
SELECT station_id, YEAR(date), MONTH(date), SUM(value_mm), SUM(value_mm >= 1), COUNT(*) 
FROM weather_observation
WHERE quality = 'G'
GROUP BY station_id, YEAR(date), MONTH(date)""");

        System.out.println("Monthly precipitation table done!");
    }
}
