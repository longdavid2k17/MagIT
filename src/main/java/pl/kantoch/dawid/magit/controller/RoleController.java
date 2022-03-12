package pl.kantoch.dawid.magit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kantoch.dawid.magit.security.user.repositories.RoleRepository;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin("*")
public class RoleController
{
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll()
    {
        return ResponseEntity.ok().body(roleRepository.findAll());
    }
}
