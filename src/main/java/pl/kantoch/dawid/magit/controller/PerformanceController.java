package pl.kantoch.dawid.magit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kantoch.dawid.magit.services.PerformanceService;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController
{
    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/task-today/{orgId}")
    public ResponseEntity<?> getAllTaskToday(@PathVariable Long orgId){
        return performanceService.getAllTaskToday(orgId);
    }

    @GetMapping("/last-30-days/{orgId}")
    public ResponseEntity<?> getSummaryForLast30Days(@PathVariable Long orgId){
        return performanceService.getSummaryForLast30Days(orgId);
    }
}
