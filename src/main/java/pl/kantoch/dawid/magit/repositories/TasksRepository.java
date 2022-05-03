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

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Query("select task from Task task where task.deleted=false and task.project = :project and task.parentTask is null and " +
            "(lower(task.title) like :search or lower(task.description) like :search) ")
    Page<Task> findAllByDeletedFalseAndProjectAndParentTaskIsNullFitlered(@Param("project") Project project,@Param("search") String search, Pageable pageable);

    @Query("select task from Task task where task.deleted=false and task.organisation = :organisation and task.parentTask is null and " +
            "(lower(task.title) like :search or lower(task.description) like :search) ")
    Page<Task> findAllByDeletedFalseAndOrganisationAndParentTaskIsNullFiltered(@Param("organisation") Organisation organisation,@Param("search") String search, Pageable pageable);

    @Query("select task from Task task where task.deleted=false and task.team = :team and task.parentTask is null and " +
            "(lower(task.title) like :search or lower(task.description) like :search) ")
    Page<Task> findAllByDeletedFalseAndTeamAndParentTaskIsNullFiltered(@Param("team") Team team,@Param("search") String search, Pageable pageable);

    List<Task> findAllByDeletedFalseAndParentTask_Id(Long id);

    Long countAllByCompletedTrueAndDeletedFalseAndProject(Project project);
    Long countAllByDeletedFalseAndProject(Project project);

    Long countAllByDeletedFalseAndUserAndCompletedTrueAndModificationDateBetween(User user,Date date, Date date1);
    Long countAllByDeletedFalseAndTeamAndCompletedTrueAndModificationDateBetween(Team team,Date date, Date date1);
    Long countAllByDeletedFalseAndTeamAndModificationDateBetween(Team team,Date date, Date date1);
    Long countAllByDeletedFalseAndUserAndModificationDateBetween(User user,Date date, Date date1);

    @Query("select count(task) from Task task where task.deleted=false and task.completed=true and task.project=:project" +
            " and task.modificationDate between :morningDate and :eveningDate")
    Long countTodayCompletedTasks(@Param("project") Project project,@Param("morningDate") Date morningDate,@Param("eveningDate") Date eveningDate);

    Optional<Task> findByIdAndDeletedFalseAndCompletedFalse(Long id);
    Optional<Task> findByIdAndDeletedFalse(Long id);

    @Modifying
    @Query("update Task set deleted=true where id=:id")
    void setAsDeleted(@Param("id") Long id);
}
