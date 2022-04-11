package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.Project;

import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<Project,Long> {
    List<Project> findAllByOrganisation_IdAndDeletedFalse(Long id);
}
