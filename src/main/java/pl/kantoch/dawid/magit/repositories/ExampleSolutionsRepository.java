package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.ExampleSolutions;
import pl.kantoch.dawid.magit.models.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExampleSolutionsRepository extends JpaRepository<ExampleSolutions,Long> {
    Optional<ExampleSolutions> findByTask(Task task);
    Optional<ExampleSolutions> findByTask_Id(Long id);
    Page<ExampleSolutions> findAllByOrganisation_Id(Long id, Pageable pageable);

    @Query("select examples from ExampleSolutions examples where examples.organisation.id = :id and " +
    "(LOWER(examples.description) like :search or LOWER(examples.type) like :search or LOWER(examples.task.title) like :search or " +
            "LOWER(examples.task.description) like :search)")
    Page<ExampleSolutions> findByQueryParam(@Param("id") Long id,@Param("search") String search, Pageable pageable);

    @Query("select example.type from ExampleSolutions example where example.organisation.id=:id")
    List<String> getAllTypesForOrganisation(@Param("id") Long id);
}
