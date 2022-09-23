package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.OrganisationRole;
import pl.kantoch.dawid.magit.models.TeamMember;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMembersRepository extends JpaRepository<TeamMember,Long> {
    List<TeamMember> findAllByTeam_Id(Long id);
    List<TeamMember> findAllByRole(OrganisationRole role);

    Optional<TeamMember> findByUser_IdAndTeam_Id(Long id, Long teamId);
}
