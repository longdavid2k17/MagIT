package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.Task;
import pl.kantoch.dawid.magit.models.TaskRealization;
import pl.kantoch.dawid.magit.security.user.User;

import java.util.Optional;

@Repository
public interface TaskRealizationRepository extends JpaRepository<TaskRealization,Long> {
    Optional<TaskRealization> findAllByTaskAndUser_IdAndStatusEquals(Task task, Long id,String status);
    Optional<TaskRealization> findByUserAndStatusEquals(User user, String status);
}
