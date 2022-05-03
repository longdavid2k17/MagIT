package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.TaskAttachment;

import java.util.List;

@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment,Long> {
    List<TaskAttachment> findAllByTaskId(Long id);
}
