package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.Team;

import java.util.List;

@Repository
public interface TeamsRepository extends JpaRepository<Team,Long> {
    List<Team> findAllByDeletedFalseAndOrganisationId(Long id);
    Page<Team> findAllByDeletedFalseAndOrganisationId(Long id, Pageable pageable);
}
