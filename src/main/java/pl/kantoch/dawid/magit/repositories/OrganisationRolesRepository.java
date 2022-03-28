package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.OrganisationRole;

import java.util.List;

@Repository
public interface OrganisationRolesRepository extends JpaRepository<OrganisationRole,Long>
{
    List<OrganisationRole> findAllByOrganisationId(Long id);
}
