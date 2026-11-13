package org.example.weatherbase.station;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {


    long countByActiveTrue();

    long countByActiveFalse();

    Station[] findAllByActiveTrue();
}
