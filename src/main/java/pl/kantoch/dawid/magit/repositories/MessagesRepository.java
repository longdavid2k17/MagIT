package pl.kantoch.dawid.magit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kantoch.dawid.magit.models.Message;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByAuthorUserIdOrTargetUserId(Long authorId, Long targetId);
    List<Message> findAllByAuthorUserIdAndTargetUserId(Long authorId, Long targetId);
}
