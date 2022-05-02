package pl.kantoch.dawid.magit.services;

import org.springframework.stereotype.Service;
import pl.kantoch.dawid.magit.repositories.ExampleSolutionsRepository;

@Service
public class ExampleSolutionsService
{
    private final ExampleSolutionsRepository exampleSolutionsRepository;

    public ExampleSolutionsService(ExampleSolutionsRepository exampleSolutionsRepository) {
        this.exampleSolutionsRepository = exampleSolutionsRepository;
    }
}
