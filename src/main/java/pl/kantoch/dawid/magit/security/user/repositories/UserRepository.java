package pl.kantoch.dawid.magit.security.user.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.security.user.Role;
import pl.kantoch.dawid.magit.security.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    List<User> findAllByOrganisation_Id(Long id);
    Page<User> findAllByOrganisation_Id(Long id, Pageable pageable);

    List<User> findAllByOrganisation_IdAndRolesContains(Long id,Role role);
}
