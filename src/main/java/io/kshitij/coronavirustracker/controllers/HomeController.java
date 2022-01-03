package io.kshitij.coronavirustracker.controllers;


import io.kshitij.coronavirustracker.models.LocationStats;
import io.kshitij.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.IntStream;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/allStats")
    public String allStats(Model model) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();


        model.addAttribute("locationStats", allStats);

        return "allStats";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        model.addAttribute("totalReportedCases", totalReportedCases);
        return "index";
    }
}
