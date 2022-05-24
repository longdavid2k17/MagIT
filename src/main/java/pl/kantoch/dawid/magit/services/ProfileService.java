package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.OrganisationRole;
import pl.kantoch.dawid.magit.repositories.OrganisationRolesRepository;
import pl.kantoch.dawid.magit.security.JWTUtils;
import pl.kantoch.dawid.magit.security.user.ERole;
import pl.kantoch.dawid.magit.security.user.Role;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.RoleRepository;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService
{
    private final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);

    private final UserRepository userRepository;
    private final OrganisationRolesRepository organisationRolesRepository;
    private final PasswordEncoder encoder;
    private final JWTUtils jwtUtils;
    private final RoleRepository roleRepository;

    public ProfileService(UserRepository userRepository,
                          OrganisationRolesRepository organisationRolesRepository,
                          PasswordEncoder encoder,
                          JWTUtils jwtUtils,
                          RoleRepository roleRepository)
    {
        this.userRepository = userRepository;
        this.organisationRolesRepository = organisationRolesRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<?> getUserById(Long id)
    {
        try
        {
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent())
                return ResponseEntity.ok().body(user);
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono użytkownika o ID="+id));
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProfileService.getUserById for id={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania danych użytkownika dla ID="+id+". Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> save(User user)
    {
        try
        {
            Optional<User> userDb = userRepository.findById(user.getId());
            if(userDb.isPresent())
            {
                if(!user.getPassword().equals(userDb.get().getPassword()))
                    user.setPassword(encoder.encode(user.getPassword()));
                User saved = userRepository.save(user);
                return ResponseEntity.ok().body(saved);
            }
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono użytkownika!"));
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProfileService.save for entity {}. Message: {}",user.toString(),e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas zapisu profilu! Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getByOrganisationId(Long id, Pageable pageable,String search)
    {
        try
        {
            Page<User> users;
            if(search==null) users = userRepository.findAllByOrganisation_IdAndIsDeletedFalse(id,pageable);
            else users = userRepository.findAllByOrganisation_IdAndNameContainsOrSurnameContainsOrEmailContainsOrUsernameContainsAndIsDeletedFalse(id,pageable,search,search,search,search);
            return ResponseEntity.ok().body(users);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProfileService.getByCompanyId for id={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania użytkowników dla organizacji o ID="+id+". Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getByOrganisationId(Long id)
    {
        try
        {
            List<User> users = userRepository.findAllByOrganisation_IdAndIsDeletedFalse(id);
            return ResponseEntity.ok().body(users);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProfileService.getByCompanyId for id={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania użytkowników dla organizacji o ID="+id+". Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getByOrganisationIdAndRoleId(Long orgId,Long roleId)
    {
        try
        {
            Optional<OrganisationRole> optional = organisationRolesRepository.findById(roleId);
            if(optional.isPresent())
            {
                List<User> allRoleUsers = userRepository.findAllByOrganisation_IdAndOrganisationRolesContainsAndIsDeletedFalse(orgId,optional.get());
                return ResponseEntity.ok().body(allRoleUsers);
            }
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono wskazanej roli!"));
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProfileService.getByOrganisationIdAndRoleId for orgId {} and roleId {}. Message: {}",orgId,roleId,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania użytkowników z rolą o ID="+roleId+". Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> forceEnableAccount(Long userId, String token) {
        if(!isAdmin(token)) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak uprawnień do wykonania tej operacji!"));
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono wskazanego użytkownika!"));
            User user = userOptional.get();
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in ProfileService.forceEnableAccount for userId {}. Message: {}",userId,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas aktywowania użytkownika o ID="+userId+". Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> forceDisableAccount(Long userId, String token) {
        if(!isAdmin(token)) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak uprawnień do wykonania tej operacji!"));
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono wskazanego użytkownika!"));
            User user = userOptional.get();
            user.setEnabled(false);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in ProfileService.forceDisableAccount for userId {}. Message: {}",userId,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas dezaktywowania użytkownika o ID="+userId+". Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> removeAccount(Long userId, String token) {
        if(!isAdmin(token)) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak uprawnień do wykonania tej operacji!"));
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono wskazanego użytkownika!"));
            User user = userOptional.get();
            user.setDeleted(true);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in ProfileService.removeAccount for userId {}. Message: {}",userId,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas usuwania użytkownika o ID="+userId+". Komunikat: "+e.getMessage()));
        }
    }

    private boolean isAdmin(String token){
        String username = jwtUtils.getUsernameFromJwtToken(token.substring(7));
        Optional<Role> optionalAdminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
        Optional<User> optionalAdmin = userRepository.findByUsernameAndRolesContainingAndIsDeletedFalse(username, optionalAdminRole.get());
        return optionalAdmin.isPresent();
    }
}
