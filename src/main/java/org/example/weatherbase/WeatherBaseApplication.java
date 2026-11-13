package org.example.weatherbase;

import org.example.weatherbase.service.SmhiImportService;
import org.example.weatherbase.station.Station;
import org.example.weatherbase.station.StationRepository;
import org.example.weatherbase.weatherobservation.WeatherObservation;
import org.example.weatherbase.weatherobservation.WeatherObservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherBaseApplication {


    public static void main(String[] args) {
        SpringApplication.run(WeatherBaseApplication.class, args);
    }

    private boolean print = false;

    @Bean
    CommandLineRunner runImport(SmhiImportService importService, WeatherObservationRepository weatherObservationRepository, StationRepository stationRepository) {
        return args -> {


            if (print) {
                int counter = 0;
//                for (WeatherObservation o : weatherObservationRepository.findAll()) {
//                    counter++;
//                    System.out.println(counter + ". " + o.getAllInfo());
//                }
                counter = 0;
                for (Station s : stationRepository.findAll()) {
                    counter++;
                    System.out.println(counter + ". " + s.getName() + "\n" + s.getLatitude() + " " + s.getLongitude() + "\n");
                }
            }
//            System.out.println("Saved " + importService.importActiveStations().size() + " active stations");
//            System.out.println("Visby " + importService.importArchive(78400L) + " archive rows");
            importService.importActiveStations();
            importService.importInactiveStations();
            System.out.println("Total stations: " + stationRepository.count());
            System.out.println("Active stations: " + stationRepository.countByActiveTrue());
            System.out.println("Inactive stations: " + stationRepository.countByActiveFalse());
            importService.importAllNewObservationsActiveStations(false);
            importService.importAllArchiveObservations(false);



//            WeatherObservation today = weatherObservationRepository.findAll().getLast();
//            System.out.println(today.getAllInfo());

            System.out.println("Counting...");
            long observationCount = weatherObservationRepository.count();

            System.out.println( observationCount/1000000 + " million Total observations");
            System.out.println( observationCount + " observations");

            weatherObservationRepository.findTop10ByQualityOrderByValueMmDesc("G")
                    .forEach(o -> System.out.println(o.getAllInfo()));
        };
    }
}