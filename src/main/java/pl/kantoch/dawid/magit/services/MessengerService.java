package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kantoch.dawid.magit.models.Message;
import pl.kantoch.dawid.magit.models.messenger.MessageWrapper;
import pl.kantoch.dawid.magit.models.messenger.MessageWrapperEntity;
import pl.kantoch.dawid.magit.repositories.MessagesRepository;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.kantoch.dawid.magit.utils.CollectionsUtils.distinctByKey;

@Service
public class MessengerService
{
    private final Logger LOGGER = LoggerFactory.getLogger(MessengerService.class);

    private final MessagesRepository messagesRepository;
    private final UserRepository userRepository;

    public MessengerService(MessagesRepository messagesRepository, UserRepository userRepository) {
        this.messagesRepository = messagesRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getAllContacts(Long id)
    {
        try
        {
            List<Message> messages = messagesRepository.findAllByAuthorUserIdOrTargetUserId(id,id);
            MessageWrapper wrapper = getWrapper(messages,id);
            return ResponseEntity.ok().body(wrapper);
        }
        catch (Exception e)
        {
            LOGGER.error("Error MessengerService.getAllContacts for ID={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas pobierania listy kontaktów! Komunikat: "+e.getMessage());
        }
    }

    public MessageWrapper getWrapper(List<Message> sourceList,Long id)
    {
        List<Message> filteredWithDistinctAuthor = sourceList.stream().filter(distinctByKey(Message::getAuthorUserId)).filter(e->!e.getAuthorUserId().equals(id)).collect(Collectors.toList());
        List<Message> filteredWithDistinctTarget = sourceList.stream().filter(distinctByKey(Message::getTargetUserId)).filter(e->!e.getTargetUserId().equals(id)).collect(Collectors.toList());
        MessageWrapper wrapper = new MessageWrapper();
        wrapper.setAllUnread(sourceList.stream().filter(e->e.getTargetUserId().equals(id) && !e.getRead()).filter(distinctByKey(Message::getAuthorUserId)).count());
        List<MessageWrapperEntity> entityList = new ArrayList<>();
        for(Message messageIncome : filteredWithDistinctAuthor)
        {
            MessageWrapperEntity entity = new MessageWrapperEntity();
            Optional<User> optionalUser = userRepository.findById(messageIncome.getAuthorUserId());
            optionalUser.ifPresent(entity::setInterlocutor);
            List<Message> all = sourceList.stream().filter(e->e.getAuthorUserId().equals(messageIncome.getAuthorUserId())).collect(Collectors.toList());
            entity.setUnread(all.stream().filter(e->!e.getRead()).count());
            entity.setMessages(all);
            entityList.add(entity);
        }
        for(Message messageOutcome : filteredWithDistinctTarget)
        {
            Optional<MessageWrapperEntity> optional = entityList.stream().filter(e->e.getInterlocutor().getId().equals(messageOutcome.getTargetUserId())).findAny();
            if(optional.isPresent())
            {
                MessageWrapperEntity entity = optional.get();
                entityList.remove(entity);
                List<Message> messages = entity.getMessages();
                List<Message> allOutcomeMessages = sourceList.stream().filter(e->e.getTargetUserId().equals(messageOutcome.getTargetUserId())).collect(Collectors.toList());
                List<Message> fullList = Stream.concat(messages.stream(), allOutcomeMessages.stream()).sorted(Comparator.comparing(Message::getSendDate,Comparator.reverseOrder())).collect(Collectors.toList());
                entity.setMessages(fullList);
                entityList.add(entity);
            }
            else
            {
                MessageWrapperEntity entity = new MessageWrapperEntity();
                Optional<User> optionalUser = userRepository.findById(messageOutcome.getTargetUserId());
                optionalUser.ifPresent(entity::setInterlocutor);
                List<Message> all = sourceList.stream().filter(e->e.getTargetUserId().equals(messageOutcome.getTargetUserId())).collect(Collectors.toList());
                entity.setUnread(0L);
                entity.setMessages(all.stream().sorted(Comparator.comparing(Message::getSendDate,Comparator.reverseOrder())).collect(Collectors.toList()));
                entityList.add(entity);
            }
        }
        wrapper.setEntityList(entityList);
        return wrapper;
    }
}
