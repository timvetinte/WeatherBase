package org.example.weatherbase;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class WeatherBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherBaseApplication.class, args);
    }

    @Bean
    CommandLineRunner testaSmhi() {
        return args -> {
            RestClient client = RestClient.create();

            String svar = client.get()
                    .uri("https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/5.json")
                    .retrieve()
                    .body(String.class);

            System.out.println(svar.substring(0, 500));
        };
    }
}