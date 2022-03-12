package pl.kantoch.dawid.magit.security.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.security.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    Optional<User> findByUsername(String username);

    User findByEmail(String email);
}