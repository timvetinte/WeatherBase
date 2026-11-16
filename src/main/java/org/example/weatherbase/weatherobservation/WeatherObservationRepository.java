package org.example.weatherbase.weatherobservation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface WeatherObservationRepository extends JpaRepository<WeatherObservation, Long> {


    @Query("select o.date from WeatherObservation  o where o.station.id = :stationId")
    Set<LocalDate> findDatesByStationId(Long stationId);

    List<WeatherObservation> findTop10ByQualityOrderByValueMmDesc(String quality);

    interface RainAvgByMonth {
        Integer getMonth();
        BigDecimal getAvgMm();
    }

    interface RainTotalByMonth {
        Integer getMonth();
        BigDecimal getTotalMm();
    }

    @Query(value = """
    SELECT month, SUM(total_mm) / SUM(days) AS avgMm
    FROM monthly_precipitation
    GROUP BY month
    ORDER BY month """,
            nativeQuery = true)
    List<RainAvgByMonth> rainAvgByMonth();


    @Query(value = """
    SELECT month, SUM(total_mm) AS totalMm
    FROM monthly_precipitation
    GROUP BY month
    ORDER BY month """,
            nativeQuery = true)
    List<RainTotalByMonth> rainTotalByMonth();

}
