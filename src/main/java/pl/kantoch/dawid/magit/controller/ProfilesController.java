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
    public ResponseEntity<?> getByOrganisationId(@PathVariable Long id, Pageable pageable,@RequestParam(required = false) String search)
    {
        return profileService.getByOrganisationId(id,pageable,search);
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

    @GetMapping("/enable-account/{userId}")
    public ResponseEntity<?> forceEnableAccount(@PathVariable Long userId,@RequestHeader("Authorization")String token){
        return profileService.forceEnableAccount(userId,token);
    }

    @GetMapping("/disable-account/{userId}")
    public ResponseEntity<?> forceDisableAccount(@PathVariable Long userId,@RequestHeader("Authorization")String token){
        return profileService.forceDisableAccount(userId,token);
    }

    @DeleteMapping("/remove-account/{userId}")
    public ResponseEntity<?> removeAccount(@PathVariable Long userId,@RequestHeader("Authorization")String token){
        return profileService.removeAccount(userId,token);
    }
}
