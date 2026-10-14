package org.example.weatherbase.weatherobservation;


import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherObservationRepository extends JpaRepository<WeatherObservation, Long> {
}
