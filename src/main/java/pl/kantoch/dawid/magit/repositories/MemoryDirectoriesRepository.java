package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.MemoryDirectory;

import java.util.Optional;

@Repository
public interface MemoryDirectoriesRepository extends JpaRepository<MemoryDirectory,Long> {
    Optional<MemoryDirectory> findByProject_Id(long id);
}
