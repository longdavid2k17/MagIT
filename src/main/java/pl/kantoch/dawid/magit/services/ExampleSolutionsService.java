package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.ExampleSolutions;
import pl.kantoch.dawid.magit.models.Organisation;
import pl.kantoch.dawid.magit.repositories.ExampleSolutionsRepository;
import pl.kantoch.dawid.magit.repositories.OrganisationsRepository;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ExampleSolutionsService
{
    private final Logger LOGGER = LoggerFactory.getLogger(ExampleSolutionsService.class);

    private final ExampleSolutionsRepository exampleSolutionsRepository;
    private final OrganisationsRepository organisationsRepository;

    public ExampleSolutionsService(ExampleSolutionsRepository exampleSolutionsRepository,
                                   OrganisationsRepository organisationsRepository) {
        this.exampleSolutionsRepository = exampleSolutionsRepository;
        this.organisationsRepository = organisationsRepository;
    }

    public ResponseEntity<?> getAllForOrganisation(Long id, Pageable pageable) {
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(id);
            if(optionalOrganisation.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono organizacji użytkownika!"));
            Page<ExampleSolutions> page = exampleSolutionsRepository.findAllByOrganisation_Id(id,pageable);
            return ResponseEntity.ok().body(page);
        }
        catch (Exception e){
            LOGGER.error("Error in ExampleSolutionsService.getAllForOrganisation for id={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania danych. Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllForOrganisationFilter(Long id, Pageable pageable,String query) {
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(id);
            if(optionalOrganisation.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono organizacji użytkownika!"));
            query="%"+query.toLowerCase(Locale.ROOT)+"%";
            Page<ExampleSolutions> page = exampleSolutionsRepository.findByQueryParam(id,query,pageable);
            return ResponseEntity.ok().body(page);
        }
        catch (Exception e){
            LOGGER.error("Error in ExampleSolutionsService.getAllForOrganisation for id={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania danych. Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> save(ExampleSolutions exampleSolutions) {
        try {
            if(exampleSolutions.getId()==null){
                Optional<ExampleSolutions> optional = exampleSolutionsRepository.findByTask(exampleSolutions.getTask());
                if(optional.isPresent()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Do tego zadania istnieje wpis rozwiązania. Zmodyfikuj go, lub usuń aby dodać nowy wpis!"));
            }
            ExampleSolutions saved = exampleSolutionsRepository.save(exampleSolutions);
            return ResponseEntity.ok().body(saved);
        }
        catch (Exception e){
            LOGGER.error("Error in ExampleSolutionsService.exampleSolutions for entity={}. Message: {}",exampleSolutions,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas zapisywania rozwiązania. Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> delete(Long id) {
        try {
            Optional<ExampleSolutions> optional = exampleSolutionsRepository.findByTask_Id(id);
            if(optional.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono rozwiązania powiązanego z tym zadaniem!"));
            exampleSolutionsRepository.delete(optional.get());
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in ExampleSolutionsService.delete for id={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas usuwania rozwiązania. Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllTypesForOrganisation(Long id) {
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(id);
            if(optionalOrganisation.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono organizacji użytkownika!"));
            List<String> list = exampleSolutionsRepository.getAllTypesForOrganisation(id);
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in ExampleSolutionsService.getAllForOrganisation for id={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania danych. Komunikat: "+e.getMessage()));
        }
    }
}
