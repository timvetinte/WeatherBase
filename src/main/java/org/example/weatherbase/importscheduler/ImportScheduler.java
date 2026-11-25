package org.example.weatherbase.importscheduler;


import lombok.RequiredArgsConstructor;
import org.example.weatherbase.monthlyprecipitation.MonthlyPrecipitationRepository;
import org.example.weatherbase.monthlyprecipitation.MonthlyPrecipitationService;
import org.example.weatherbase.service.SmhiImportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImportScheduler {

    private final SmhiImportService smhiImportService;
    private final MonthlyPrecipitationService monthlyPrecipitationService;


    @Scheduled(cron = "0 0 9 * * *", zone = "Europe/Stockholm")
    public void dailyImport() throws InterruptedException {
        smhiImportService.importAllNewObservationsActiveStations(true);
        monthlyPrecipitationService.refreshMonthly();
    }
}
