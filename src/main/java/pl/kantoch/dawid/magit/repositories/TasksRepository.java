package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.Organisation;
import pl.kantoch.dawid.magit.models.Project;
import pl.kantoch.dawid.magit.models.Task;
import pl.kantoch.dawid.magit.models.Team;
import pl.kantoch.dawid.magit.security.user.User;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Task,Long> {
    List<Task> findAllByDeletedFalseAndProject(Project project);
    List<Task> findAllByDeletedFalseAndTeam(Team team);
    List<Task> findAllByDeletedFalseAndUser(User user);
    List<Task> findAllByDeletedFalseAndOrganisation(Organisation organisation);

    Page<Task> findAllByDeletedFalseAndProject(Project project, Pageable pageable);
    Page<Task> findAllByDeletedFalseAndTeam(Team team, Pageable pageable);
    Page<Task> findAllByDeletedFalseAndUser(User user, Pageable pageable);
    Page<Task> findAllByDeletedFalseAndOrganisation(Organisation organisation, Pageable pageable);

    @Modifying
    @Query("update Task set deleted=true where id=:id")
    void setAsDeleted(@Param("id") Long id);
}
