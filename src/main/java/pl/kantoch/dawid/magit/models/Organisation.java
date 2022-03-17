package pl.kantoch.dawid.magit.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "organisations",uniqueConstraints = {
        @UniqueConstraint(columnNames = "invite_code")
})
public class Organisation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "owner_id")
    private Long ownerId;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "invite_code")
    private String inviteCode;

    public Organisation() {
    }

    public Organisation(Long id, String name, String description, Long ownerId, Date creationDate, String inviteCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.creationDate = creationDate;
        this.inviteCode = inviteCode;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long owner) {
        this.ownerId = owner;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + ownerId +
                ", creationDate=" + creationDate +
                '}';
    }
}
