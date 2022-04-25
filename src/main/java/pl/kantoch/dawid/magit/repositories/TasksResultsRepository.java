package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.TaskResult;

@Repository
public interface TasksResultsRepository extends JpaRepository<TaskResult,Long> {
}
