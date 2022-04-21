package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectsRepository extends JpaRepository<Project,Long> {
    List<Project> findAllByOrganisation_IdAndDeletedFalse(Long id);

    Optional<Project> findByIdAndDeletedIsFalseAndArchivedIsFalse(Long id);
    Optional<Project> findByIdAndDeletedIsFalseAndArchivedIsTrue(Long id);

    @Query("select project from Project project where project.deleted = false and project.organisation.id=:id " +
            "and ((lower(project.name) like :keyword or :keyword is null) "+
            "or (lower(project.description) like :keyword or :keyword is null) "+
            "or (lower(project.driveName) like :keyword or :keyword is null)) ")
    List<Project> findBySearchKeyword(@Param("id") Long id,@Param("keyword") String keyword);
}
