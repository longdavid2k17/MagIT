package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.TeamMember;

import java.util.List;

@Repository
public interface TeamMembersRepository extends JpaRepository<TeamMember,Long> {
    List<TeamMember> findAllByTeam_Id(Long id);
}
