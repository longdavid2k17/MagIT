package pl.kantoch.dawid.magit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.services.MessengerService;

@RestController
@RequestMapping("/api/messenger")
@CrossOrigin("*")
public class MessengerController
{
    private final MessengerService messengerService;

    public MessengerController(MessengerService messengerService)
    {
        this.messengerService = messengerService;
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<?> getAllContacts(@PathVariable Long id)
    {
        return messengerService.getAllContacts(id);
    }
}
