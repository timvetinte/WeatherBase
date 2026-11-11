package org.example.weatherbase;

import io.micrometer.observation.Observation;
import org.example.weatherbase.service.SmhiImportService;
import org.example.weatherbase.station.Station;
import org.example.weatherbase.station.StationRepository;
import org.example.weatherbase.weatherobservation.WeatherObservation;
import org.example.weatherbase.weatherobservation.WeatherObservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherBaseApplication {


    public static void main(String[] args) {
        SpringApplication.run(WeatherBaseApplication.class, args);
    }

    private boolean print = false;

    @Bean
    @ConditionalOnProperty(name = "weatherbase.import-on-startup", havingValue = "true")
    CommandLineRunner runImport(SmhiImportService importService, WeatherObservationRepository weatherObservationRepository, StationRepository stationRepository) {
        return args -> {
            System.out.println("Saved " + importService.importActiveStations().size() + " stations");
            System.out.println("Saved " + importService.importObservations(78400L) + " stations");
            importService.importAllObservations(false);
            WeatherObservation today = weatherObservationRepository.findAll().getLast();
            System.out.println(today.getAllInfo());
            System.out.println(weatherObservationRepository.findAll().stream().count());

            if (print) {
                for (WeatherObservation o : weatherObservationRepository.findAll()) {
                    System.out.println(o.getAllInfo());
                }
                for (Station s : stationRepository.findAll()) {
                    System.out.println(s.getName() + "\n" + s.getLatitude() + " " + s.getLongitude() + "\n");
                }
            }
        };
    }
}