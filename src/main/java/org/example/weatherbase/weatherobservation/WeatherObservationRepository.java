package org.example.weatherbase.weatherobservation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface WeatherObservationRepository extends JpaRepository<WeatherObservation, Long> {


    @Query("select o.date from WeatherObservation  o where o.station.id = :stationId")
    Set<LocalDate> findDatesByStationId(Long stationId);

    List<WeatherObservation> findTop10ByQualityOrderByValueMmDesc(String quality);
}
