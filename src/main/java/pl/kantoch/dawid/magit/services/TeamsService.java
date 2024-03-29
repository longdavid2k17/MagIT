package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.*;
import pl.kantoch.dawid.magit.models.payloads.requests.CreateTeamRequest;
import pl.kantoch.dawid.magit.models.payloads.requests.EditTeamRequest;
import pl.kantoch.dawid.magit.repositories.*;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamsService
{
    private final Logger LOGGER = LoggerFactory.getLogger(TeamsService.class);

    private final TeamsRepository teamsRepository;
    private final TeamMembersRepository teamMembersRepository;
    private final OrganisationsRepository organisationsRepository;
    private final UserRepository userRepository;
    private final OrganisationRolesRepository organisationRolesRepository;
    private final ProjectsRepository projectsRepository;

    public TeamsService(TeamsRepository teamsRepository,
                        TeamMembersRepository teamMembersRepository,
                        OrganisationsRepository organisationsRepository,
                        UserRepository userRepository,
                        OrganisationRolesRepository organisationRolesRepository,
                        ProjectsRepository projectsRepository) {
        this.teamsRepository = teamsRepository;
        this.teamMembersRepository = teamMembersRepository;
        this.organisationsRepository = organisationsRepository;
        this.userRepository = userRepository;
        this.organisationRolesRepository = organisationRolesRepository;
        this.projectsRepository = projectsRepository;
    }

    public ResponseEntity<?> getAllTeamsInOrganisation(Long id, Pageable pageable)
    {
        try
        {
            Page<Team> teams = teamsRepository.findAllByDeletedFalseAndOrganisationId(id,pageable);
            return ResponseEntity.ok().body(teams);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in TeamsService.getAllTeamsInOrganisation for ID {}. Error message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas pobierania zespołów! Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllTeamsInOrganisation(Long id)
    {
        try
        {
            List<Team> teams = teamsRepository.findAllByDeletedFalseAndOrganisationId(id);
            return ResponseEntity.ok().body(teams);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in TeamsService.getAllTeamsInOrganisation for ID {}. Error message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas pobierania zespołów! Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllUserTeamsNoPage(Long id){
        try
        {
            List<Team> teams = teamsRepository.findForUser(id);
            return ResponseEntity.ok().body(teams);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in TeamsService.getAllUserTeamsNoPage for ID {}. Error message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas pobierania zespołów użytkownika! Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> save(CreateTeamRequest request)
    {
        try
        {
            if(request.getTeam()==null || request.getTeamMembers()==null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błędne zapytanie zapisu!"));
            if(request.getTeam().getOrganisationId()==null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak informacji o organizacji!"));
            Optional<Organisation> optionalTeam = organisationsRepository.findById(request.getTeam().getOrganisationId());
            if(optionalTeam.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono wskazanej organizacji!"));

            Team team = new Team();
            team.setName(request.getTeam().getName());
            team.setDescription(request.getTeam().getDescription());
            team.setOrganisationId(optionalTeam.get().getId());
            team.setDeleted(false);
            if(request.getTeam().getTeamLeader()!=null){
                Optional<User> optionalUser = userRepository.findById(request.getTeam().getTeamLeader().getId());
                if(optionalUser.isEmpty())
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono wskazanej organizacji!"));
                team.setTeamLeader(optionalUser.get());
            }
            if(request.getTeam().getDefaultProject()!=null){
                Optional<Project> optionalProject = projectsRepository.findById(request.getTeam().getDefaultProject().getId());
                if(optionalProject.isEmpty())
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono wskazanego projektu!"));
                team.setDefaultProject(optionalProject.get());
            }
            Team saved = teamsRepository.save(team);
            if(saved.getId()==null) throw new Exception("Nie zapisano poprawnie encji! Spróbuj ponowanie!");

            return saveTeamMembers(request,saved);
        }
        catch (Exception e){
            LOGGER.error("Error in TeamsService.save(). Error message: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas próby zapisu zespołu! Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> saveTeamMembers(CreateTeamRequest request, Team savedTeam) {
        try {
            List<TeamMember> toSave = new ArrayList<>();
            request.getTeamMembers().forEach(e->{
                TeamMember member = new TeamMember();
                member.setTeam(savedTeam);
                Optional<OrganisationRole> roleOptional = organisationRolesRepository.findById(e.getRole().getId());
                roleOptional.ifPresent(member::setRole);
                Optional<User> userOptional = userRepository.findById(e.getUser().getId());
                userOptional.ifPresent(member::setUser);
                toSave.add(member);
            });
            teamMembersRepository.saveAll(toSave);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in TeamsService.saveTeamMembers(). Error message: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas próby zapisu członków zespołu! Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> edit(EditTeamRequest request)
    {
        try {
            if(request.getTeam()==null || request.getTeamMembers()==null || request.getTeamMembers().isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie można zapisać zmian! Przesłane żądanie jest błędne!"));
            request.getTeam().setDeleted(false);
            Team edited = teamsRepository.save(request.getTeam());
            return editTeamMembers(request,edited);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in TeamsService.edit(). Error message: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas próby zapisu zespołu! Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> editTeamMembers(EditTeamRequest request, Team savedTeam) {
        try {
            List<TeamMember> toSave = new ArrayList<>();
            request.getTeamMembers().forEach(e->{
                if(e.getId()==null){
                    TeamMember member = new TeamMember();
                    member.setTeam(savedTeam);
                    Optional<OrganisationRole> roleOptional = organisationRolesRepository.findById(e.getRole().getId());
                    roleOptional.ifPresent(member::setRole);
                    Optional<User> userOptional = userRepository.findById(e.getUser().getId());
                    userOptional.ifPresent(member::setUser);
                    toSave.add(member);
                }
            });
            teamMembersRepository.saveAll(toSave);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in TeamsService.editTeamMembers(). Error message: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas próby zapisu członków zespołu! Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> deleteTeam(Long id)
    {
        try
        {
            Optional<Team> optionalTeam = teamsRepository.findById(id);
            if(optionalTeam.isPresent())
            {
                Team team = optionalTeam.get();
                team.setDeleted(true);
                teamsRepository.save(team);
            }
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            LOGGER.error("Error in TeamsService.deleteTeam for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas usuwania zespołu o ID="+id+". Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllTeamMembersNoPage(Long id) {
        try {
            List<TeamMember> teamMembers = teamMembersRepository.findAllByTeam_Id(id);
            return ResponseEntity.ok().body(teamMembers);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in TeamsService.getAllTeamMembersNoPage for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania członków zespołu o ID="+id+". Komunikat: "+e.getMessage()));
        }
    }
}
