package pl.kantoch.dawid.magit.security.user;

import pl.kantoch.dawid.magit.models.Organisation;
import pl.kantoch.dawid.magit.models.OrganisationRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    private String username;

    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 120)
    private String password;

    @Size(max = 500)
    private String bio;

    @Size(max = 50)
    private String name;

    @Size(max = 50)
    private String surname;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_organisation_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<OrganisationRole> organisationRoles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @Column(name = "last_logged")
    private Date lastLogged;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public User()
    {
        super();
        this.enabled=false;
    }

    public User(Long id,String name, String surname)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public User(Long id, String username, String email, String password, String bio, String name, String surname, boolean enabled, Set<Role> roles)
    {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.roles = roles;
    }

    public User(Long id, String username, String email, String password, String bio, String name, String surname, Set<Role> roles)
    {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.name = name;
        this.surname = surname;
        this.roles = roles;
    }

    public User(Long id, String username, String email, String password, String bio, String name, String surname, boolean enabled, Set<Role> roles, Set<OrganisationRole> organisationRoles, Organisation organisation, Date lastLogged)
    {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.roles = roles;
        this.organisationRoles = organisationRoles;
        this.organisation = organisation;
        this.lastLogged = lastLogged;
    }

    public User(Long id, String username, String email, String password, String bio, String name, String surname, boolean enabled, Set<Role> roles, Set<OrganisationRole> organisationRoles, Organisation organisation, Date lastLogged, Boolean isDeleted) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.roles = roles;
        this.organisationRoles = organisationRoles;
        this.organisation = organisation;
        this.lastLogged = lastLogged;
        this.isDeleted = isDeleted;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Set<OrganisationRole> getOrganisationRoles() {
        return organisationRoles;
    }

    public void setOrganisationRoles(Set<OrganisationRole> organisationRoles) {
        this.organisationRoles = organisationRoles;
    }

    public Date getLastLogged() {
        return lastLogged;
    }

    public void setLastLogged(Date lastLogged) {
        this.lastLogged = lastLogged;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", organisationRoles=" + organisationRoles +
                ", organisation=" + organisation +
                ", lastLogged=" + lastLogged +
                ", isDeleted=" + isDeleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return enabled == user.enabled && Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(bio, user.bio) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, bio, name, surname, enabled, roles);
    }
}
