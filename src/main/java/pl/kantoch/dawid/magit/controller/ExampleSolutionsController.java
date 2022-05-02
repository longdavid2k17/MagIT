package pl.kantoch.dawid.magit.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kantoch.dawid.magit.services.ExampleSolutionsService;

@RestController
@RequestMapping("/api/solutions")
public class ExampleSolutionsController
{
    private final ExampleSolutionsService exampleSolutionsService;

    public ExampleSolutionsController(ExampleSolutionsService exampleSolutionsService) {
        this.exampleSolutionsService = exampleSolutionsService;
    }
}
