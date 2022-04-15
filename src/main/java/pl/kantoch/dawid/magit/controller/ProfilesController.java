package pl.kantoch.dawid.magit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.services.ProfileService;

@RestController
@RequestMapping("/api/profiles")
public class ProfilesController
{
    private final ProfileService profileService;

    public ProfilesController(ProfileService profileService)
    {
        this.profileService = profileService;
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id)
    {
        return profileService.getUserById(id);
    }

    @PutMapping("save")
    public ResponseEntity<?> getById(@RequestBody User user)
    {
        return profileService.save(user);
    }

    @GetMapping("/by-organisation/{id}")
    public ResponseEntity<?> getByOrganisationId(@PathVariable Long id, Pageable pageable)
    {
        return profileService.getByOrganisationId(id,pageable);
    }

    @GetMapping("/by-organisation-nopage/{id}")
    public ResponseEntity<?> getByOrganisationIdNoPage(@PathVariable Long id)
    {
        return profileService.getByOrganisationId(id);
    }

    @GetMapping("/by-organisation-and-role-nopage/{orgId}/{roleId}")
    public ResponseEntity<?> getByOrganisationIdAndRoleIdNoPage(@PathVariable Long orgId,@PathVariable Long roleId)
    {
        return profileService.getByOrganisationIdAndRoleId(orgId,roleId);
    }
}
