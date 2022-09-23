package pl.kantoch.dawid.magit.security.user.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.OrganisationRole;
import pl.kantoch.dawid.magit.security.user.Role;
import pl.kantoch.dawid.magit.security.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    Optional<User> findByUsernameAndRolesContainingAndIsDeletedFalse(String username,Role role);

    User findByEmailAndIsDeletedFalse(String email);

    List<User> findAllByOrganisation_IdAndIsDeletedFalse(Long id);
    Page<User> findAllByOrganisation_IdAndIsDeletedFalse(Long id, Pageable pageable);
    Page<User> findAllByOrganisation_IdAndNameContainsOrSurnameContainsOrEmailContainsOrUsernameContainsAndIsDeletedFalse(Long id, Pageable pageable,String param1,String param2,String param3,String param4);

    List<User> findAllByOrganisation_IdAndRolesContainsAndIsDeletedFalse(Long id,Role role);
    List<User> findAllByOrganisation_IdAndOrganisationRolesContainsAndIsDeletedFalse(Long id, OrganisationRole role);

    List<User> findAllByOrganisationRolesContainsAndIsDeletedFalse(OrganisationRole role);
}
