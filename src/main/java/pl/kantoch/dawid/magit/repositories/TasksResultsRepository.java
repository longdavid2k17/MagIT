package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.TaskResult;

import java.util.List;

@Repository
public interface TasksResultsRepository extends JpaRepository<TaskResult,Long> {
    List<TaskResult> findAllByTaskId(Long id);
}
