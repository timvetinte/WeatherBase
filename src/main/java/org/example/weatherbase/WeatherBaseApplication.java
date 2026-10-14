package org.example.weatherbase;

import io.micrometer.observation.Observation;
import org.example.weatherbase.smhi.SmhiObservation;
import org.example.weatherbase.smhi.SmhiObservationResponse;
import org.example.weatherbase.smhi.SmhiStation;
import org.example.weatherbase.smhi.SmhiStationResponse;
import org.example.weatherbase.station.Station;
import org.example.weatherbase.station.StationRepository;
import org.example.weatherbase.weatherobservation.WeatherObservation;
import org.example.weatherbase.weatherobservation.WeatherObservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class WeatherBaseApplication {

    public static void main(String[] args) {

        SpringApplication.run(WeatherBaseApplication.class, args);
    }



    @Bean
    CommandLineRunner testaSmhi(StationRepository stationRepository, WeatherObservationRepository weatherObservationRepository) {
        return args -> {
            RestClient client = RestClient.create();

            SmhiStationResponse response = client.get()
                    .uri("https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/5.json")
                    .retrieve()
                    .body(SmhiStationResponse.class);

            long activeStations = response.getStation().stream().filter(s -> s.isActive()).count();
            long inactiveStations = response.getStation().stream().filter(s -> !s.isActive()).count();

            List <Station> activeStationsEntities = response.getStation().stream().filter(s -> s.isActive()).map(WeatherBaseApplication::toStation).toList();

            System.out.println("Amount of active stations today: " + activeStations);
            System.out.println("Amount of inactive station today: " + inactiveStations);
            System.out.println(activeStationsEntities.size());

            stationRepository.saveAll(activeStationsEntities);
            System.out.println("Saved " + activeStationsEntities.size() + " stations");

            System.out.println("Visby Flygplats: " + findByName("Visby Flygplats", activeStationsEntities));


            Station station = stationRepository.findById(78400L).orElseThrow();


            SmhiObservationResponse observationResponse = client.get()
                    .uri("https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/5/station/78400/period/latest-months/data.json")
                    .retrieve()
                    .body(SmhiObservationResponse.class);

            List <WeatherObservation> observationEntities = observationResponse.getValue().stream().map(o -> toWeatherObservation(o, station)).toList();

            weatherObservationRepository.saveAll(observationEntities);
            System.out.println("Saved " + observationEntities.size() + " observations");
            System.out.println(observationResponse);

        };
    }
    private Long findByName(String name, List<Station> allStations){
        return allStations.stream().filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().get().getId();
    }

    private static Station toStation(SmhiStation s){
        Station station = new Station();
        station.setId(s.getId());
        station.setName(s.getName());
        station.setLongitude(s.getLongitude());
        station.setLatitude(s.getLatitude());
        station.setActive(s.isActive());
        return station;
    }

    private static WeatherObservation toWeatherObservation(SmhiObservation o, Station station){
        WeatherObservation observation = new WeatherObservation();
        observation.setDate(LocalDate.parse(o.getRef()));
        observation.setQuality(o.getQuality());
        observation.setValueMm(new BigDecimal(o.getValue()));
        observation.setStation(station);
        return observation;
    }
}