package pl.kantoch.dawid.magit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.ExampleSolutions;
import pl.kantoch.dawid.magit.services.ExampleSolutionsService;

@RestController
@RequestMapping("/api/solutions")
public class ExampleSolutionsController
{
    private final ExampleSolutionsService exampleSolutionsService;

    public ExampleSolutionsController(ExampleSolutionsService exampleSolutionsService) {
        this.exampleSolutionsService = exampleSolutionsService;
    }

    @GetMapping("/organisation/{id}")
    public ResponseEntity<?> getAllForOrganisation(@PathVariable Long id, Pageable pageable,@RequestParam(required = false) String query){
        if(query!=null) return exampleSolutionsService.getAllForOrganisationFilter(id,pageable,query);
        else return exampleSolutionsService.getAllForOrganisation(id,pageable);
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<?> getAllTypesForOrganisation(@PathVariable Long id){
        return exampleSolutionsService.getAllTypesForOrganisation(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ExampleSolutions exampleSolutions){
        return exampleSolutionsService.save(exampleSolutions);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return exampleSolutionsService.delete(id);
    }
}
