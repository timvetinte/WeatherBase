package org.example.weatherbase;

import org.example.weatherbase.monthlyprecipitation.MonthlyPrecipitationService;
import org.example.weatherbase.service.SmhiImportService;
import org.example.weatherbase.station.Station;
import org.example.weatherbase.station.StationRepository;
import org.example.weatherbase.weatherobservation.WeatherObservation;
import org.example.weatherbase.weatherobservation.WeatherObservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;

@SpringBootApplication
public class WeatherBaseApplication {

    StopWatch sw_short = new StopWatch();
    StopWatch sw_long = new StopWatch();


    public static void main(String[] args) {
        SpringApplication.run(WeatherBaseApplication.class, args);
    }

    private boolean print = true;

    @Bean
    CommandLineRunner runImport(SmhiImportService importService, MonthlyPrecipitationService monthlyPrecipitationService, WeatherObservationRepository weatherObservationRepository, StationRepository stationRepository) {
        return args -> {



//            System.out.println("Saved " + importService.importActiveStations().size() + " active stations");
//            System.out.println("Visby " + importService.importArchive(78400L) + " archive rows");
            importService.importActiveStations();
            importService.importInactiveStations();
            System.out.println("Total stations: " + stationRepository.count());
            System.out.println("Active stations: " + stationRepository.countByActiveTrue());
            System.out.println("Inactive stations: " + stationRepository.countByActiveFalse());

            if(true) {
                sw_long.start("All imports");
                sw_short.start("New Observation import");
                importService.importAllNewObservationsActiveStations(true);
                sw_short.stop();
                sw_short.start("Monthly precipitation");
                System.out.println("Building monthly precipitation...");
                monthlyPrecipitationService.buildAll();
                sw_short.stop();
                sw_short.start("Archive import");
                importService.importAllArchiveObservations(false);
                sw_short.stop();
                sw_long.stop();
            };





//            WeatherObservation today = weatherObservationRepository.findAll().getLast();
//            System.out.println(today.getAllInfo());





            if (print) {
                sw_long.start("Print section");
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

                System.out.println("Counting...");
                sw_short.start("Counting observations and calculating");
                long observationCount = weatherObservationRepository.count();

                System.out.println(observationCount / 1000000 + " million Total observations");
                System.out.println(observationCount + " observations");
                sw_short.stop();

                sw_short.start("Top 10 by mm and quality G");
                weatherObservationRepository.findTop10ByQualityOrderByValueMmDesc("G")
                        .forEach(o -> System.out.println(o.getAllInfo()));
                sw_short.stop();

                System.out.println("Avg rain by month: ");
                sw_short.start("Average rain per month");
                for (WeatherObservationRepository.RainAvgByMonth o : weatherObservationRepository.rainAvgByMonth()) {
                    System.out.println(o.getMonth() + ": " + o.getAvgMm());
                }
                sw_short.stop();

                System.out.println("Total rain by month: ");
                sw_short.start("Total rain by month");
                for (WeatherObservationRepository.RainTotalByMonth o : weatherObservationRepository.rainTotalByMonth()) {
                    System.out.println(o.getMonth() + ": " + o.getTotalMm());
                }
                sw_short.stop();
                sw_long.stop();
                System.out.println(sw_short.prettyPrint());
                System.out.println(sw_long.prettyPrint());
            }


        };
    }
}