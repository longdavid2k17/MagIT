package pl.kantoch.dawid.magit.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "organisation_roles")
@Data
public class OrganisationRole
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String name;

    @Column(name = "icon_name")
    private String iconName;

    @Column(name = "organisation_id")
    private Long organisationId;

    public OrganisationRole(Integer id, String name, String iconName, Long organisationId) {
        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.organisationId = organisationId;
    }

    public OrganisationRole() {
    }

    public Integer getId() {
        return id;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }
}
