package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kantoch.dawid.magit.models.Organisation;
import pl.kantoch.dawid.magit.models.Project;
import pl.kantoch.dawid.magit.models.payloads.responses.SummaryChartResponse;
import pl.kantoch.dawid.magit.repositories.OrganisationsRepository;
import pl.kantoch.dawid.magit.repositories.ProjectsRepository;
import pl.kantoch.dawid.magit.repositories.TasksRepository;
import pl.kantoch.dawid.magit.utils.DateUtils;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService
{
    private final Logger LOGGER = LoggerFactory.getLogger(PerformanceService.class);

    private final TasksRepository tasksRepository;
    private final OrganisationsRepository organisationsRepository;
    private final ProjectsRepository projectsRepository;

    public PerformanceService(TasksRepository tasksRepository,
                              OrganisationsRepository organisationsRepository,
                              ProjectsRepository projectsRepository) {
        this.tasksRepository = tasksRepository;
        this.organisationsRepository = organisationsRepository;
        this.projectsRepository = projectsRepository;
    }

    public ResponseEntity<?> getAllTaskToday(Long orgId) {
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(orgId);
            if(optionalOrganisation.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono organizacji o ID="+orgId));
            LocalDateTime morningDate = LocalDate.now().atTime(8,0);
            LocalDateTime eveningDate = LocalDate.now().atTime(19,0);
            List<Long> taskCounts = new ArrayList<>();
            for(LocalDateTime ldt = morningDate; ldt.isBefore(eveningDate);ldt=ldt.plusHours(1)){
                Date dateMorningConverted =  DateUtils.convertToDateViaSqlTimestamp(ldt);
                Date dateEveningConverted =  DateUtils.convertToDateViaSqlTimestamp(ldt.plusHours(1));
                taskCounts.add(tasksRepository.countAllByDeletedFalseAndOrganisationAndCompletedTrueAndModificationDateBetween(optionalOrganisation.get(),dateMorningConverted,dateEveningConverted));
            }
            return ResponseEntity.ok().body(taskCounts);
        }
        catch (Exception e){
            LOGGER.error("Error in PerformanceService.getAllTaskToday for orgId={}. Message: {}",orgId,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas pobierania danych do dziennego zestawienia! Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getSummaryForLast30Days(Long orgId) {
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(orgId);
            if(optionalOrganisation.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono organizacji o ID="+orgId));
            LocalDateTime firstDay = LocalDate.now().minusDays(30).atStartOfDay();
            LocalDateTime lastDay = LocalTime.MAX.atDate(LocalDate.now());
            List<SummaryChartResponse> list = new ArrayList<>();
            List<Project> projectList = projectsRepository.findAllByOrganisation_IdAndDeletedFalse(orgId);
            for(Project project : projectList){
                Date dateMorningConverted =  DateUtils.convertToDateViaSqlTimestamp(firstDay);
                Date dateEveningConverted =  DateUtils.convertToDateViaSqlTimestamp(lastDay);
                Long taskCounts = tasksRepository.countAllByDeletedFalseAndProjectAndCreationDateBetween(project,dateMorningConverted,dateEveningConverted);
                list.add(new SummaryChartResponse(project.getName(),taskCounts));
            }
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in PerformanceService.getSummaryForLast30Days for orgId={}. Message: {}",orgId,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas pobierania danych do wykresu ostatnich 30 dni! Komunikat: "+e.getMessage()));
        }
    }
}
