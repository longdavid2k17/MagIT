package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.TeamMember;

@Repository
public interface TeamMembersRepository extends JpaRepository<TeamMember,Long> {
}
