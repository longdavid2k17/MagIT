package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.Team;

import java.util.List;

@Repository
public interface TeamsRepository extends JpaRepository<Team,Long> {
    List<Team> findAllByDeletedFalseAndOrganisationId(Long id);
    Page<Team> findAllByDeletedFalseAndOrganisationId(Long id, Pageable pageable);

    @Query("select team from Team team join TeamMember m on team.id = m.team.id " +
            "where team.deleted = false and m.user.id = :id")
    List<Team> findForUser(@Param("id") Long id);
}
