package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.ExampleSolutions;
import pl.kantoch.dawid.magit.models.Task;

import java.util.Optional;

@Repository
public interface ExampleSolutionsRepository extends JpaRepository<ExampleSolutions,Long> {
    Optional<ExampleSolutions> findByTask(Task task);
}
