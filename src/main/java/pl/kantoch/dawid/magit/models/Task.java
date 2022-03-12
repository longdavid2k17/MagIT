package pl.kantoch.dawid.magit.models;

import org.hibernate.annotations.CreationTimestamp;
import pl.kantoch.dawid.magit.security.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "title")
    private String title;
}
