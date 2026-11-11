package org.example.weatherbase.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherbase.smhi.SmhiClient;
import org.example.weatherbase.smhi.SmhiObservation;
import org.example.weatherbase.smhi.SmhiStation;
import org.example.weatherbase.station.Station;
import org.example.weatherbase.station.StationRepository;
import org.example.weatherbase.weatherobservation.WeatherObservation;
import org.example.weatherbase.weatherobservation.WeatherObservationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SmhiImportService {

    private Station toStation(SmhiStation s){
        Station station = new Station();
        station.setId(s.getId());
        station.setName(s.getName());
        station.setLongitude(s.getLongitude());
        station.setLatitude(s.getLatitude());
        station.setActive(s.isActive());
        return station;
    }

    private WeatherObservation toWeatherObservation(SmhiObservation o, Station station){
        WeatherObservation observation = new WeatherObservation();
        observation.setDate(LocalDate.parse(o.getRef()));
        observation.setQuality(o.getQuality());
        observation.setValueMm(new BigDecimal(o.getValue()));
        observation.setStation(station);
        return observation;
    }

    private static final int PRECIPITATION = 5;
    private final JdbcTemplate jdbcTemplate;

    private final SmhiClient smhiClient;
    private final StationRepository stationRepository;
    private final WeatherObservationRepository weatherObservationRepository;

    public List<Station> importActiveStations(){
        List<Station> stations = smhiClient.fetchStations(PRECIPITATION)
                .stream().filter(SmhiStation::isActive)
                .map(this::toStation)
                .toList();

        return stationRepository.saveAll(stations);
    }

    public int importObservations(long stationId){
        Station station = stationRepository.findById(stationId)
                .orElseThrow(()-> new IllegalArgumentException("Station missing: " + stationId));

        Set<LocalDate> seenDates = new HashSet<>(weatherObservationRepository.findDatesByStationId(stationId));

        List<WeatherObservation> observations = smhiClient.fetchLatestMonths(PRECIPITATION, stationId).stream()
                .map(o-> toWeatherObservation(o, station))
                .filter(o-> seenDates.add(o.getDate())).toList();
        
        weatherObservationRepository.saveAll(observations);
        return observations.size();
    }

//    public int importArchive(long stationId){
//        List<SmhiObservation> all = new ArrayList<>(smhiClient.fetchCorrectedArchive)
//    }

    public void importAllObservations(boolean runImport) throws InterruptedException {
        if(runImport) {
            for (Station s : stationRepository.findAll().stream().toList()) {
                try {
                    System.out.println(s.getName() + ": " + importObservations(s.getId()) + " new");
                } catch (Exception e) {
                    System.out.println("Failed station: " + s.getName() + ": " + e.getMessage());
                }
                Thread.sleep(500);
            }
        }else {
            System.out.println("Import of observations set to false");
        }
    };
}
