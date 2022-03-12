package pl.kantoch.dawid.magit.security.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.security.user.ERole;
import pl.kantoch.dawid.magit.security.user.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>
{
    Optional<Role> findByName(ERole name);
}
