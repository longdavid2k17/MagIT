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
    List<Task> findAllByDeletedFalseAndProjectAndParentTaskIsNull(Project project);
    List<Task> findAllByDeletedFalseAndTeamAndParentTaskIsNull(Team team);
    List<Task> findAllByDeletedFalseAndUserAndParentTaskIsNull(User user);
    List<Task> findAllByDeletedFalseAndOrganisationAndParentTaskIsNull(Organisation organisation);

    Page<Task> findAllByDeletedFalseAndProjectAndParentTaskIsNull(Project project, Pageable pageable);
    Page<Task> findAllByDeletedFalseAndTeamAndParentTaskIsNull(Team team, Pageable pageable);
    Page<Task> findAllByDeletedFalseAndUserAndParentTaskIsNull(User user, Pageable pageable);
    Page<Task> findAllByDeletedFalseAndOrganisationAndParentTaskIsNull(Organisation organisation, Pageable pageable);

    Long countAllByCompletedTrueAndDeletedFalseAndProject(Project project);
    Long countAllByDeletedFalseAndProject(Project project);

    @Modifying
    @Query("update Task set deleted=true where id=:id")
    void setAsDeleted(@Param("id") Long id);
}
