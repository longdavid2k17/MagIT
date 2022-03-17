package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.Organisation;

@Repository
public interface OrganisationsRepository extends JpaRepository<Organisation,Long>
{
    Organisation findByInviteCode(String inviteCode);
}
