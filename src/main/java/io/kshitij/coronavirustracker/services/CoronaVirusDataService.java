package io.kshitij.coronavirustracker.services;

import io.kshitij.coronavirustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    boolean isHeader = false;
    //one(final) instance of parsed csv data
    private List<LocationStats> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {

        //another(local/temp) instance of parsed csv data
        List<LocationStats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

//        System.out.println(httpResponse.body());

        StringReader in = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);



        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();

            //setting headers
            if(isHeader == false){
                locationStat.setState(record.get(0));
                locationStat.setCountry(record.get(1));

                String temp = record.get(record.size()-1);
                locationStat.setLatestTotalCases(temp);
                System.out.println(locationStat);
            }

            else {

                locationStat.setState(record.get(0));
                locationStat.setCountry(record.get(1));

                String temp = record.get(record.size()-1);
                locationStat.setLatestTotalCases(temp);
                System.out.println(locationStat);

                newStats.add(locationStat);
//            String customerNo = record.get("CustomerNo");
//            String name = record.get("Name");

            }

        }

        this.allStats = newStats;

        //using two instances for the same data as there will always be some data to show while processing the latest data
    }

}
