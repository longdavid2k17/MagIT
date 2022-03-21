package pl.kantoch.dawid.magit.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_id")
    @NotNull
    private Long authorUserId;

    @Column(name = "target_id")
    @NotNull
    private Long targetUserId;

    @Column(name = "text")
    @NotEmpty
    private String text;

    @Column(name = "send_date")
    @CreationTimestamp
    private Date sendDate;

    @Column(name = "is_read")
    private Boolean isRead;

    public Message() {
    }

    public Message(Long id, Long authorUserId, Long targetUserId, String text, Date sendDate, Boolean isRead) {
        this.id = id;
        this.authorUserId = authorUserId;
        this.targetUserId = targetUserId;
        this.text = text;
        this.sendDate = sendDate;
        this.isRead = isRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(Long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", authorUserId=" + authorUserId +
                ", targetUserId=" + targetUserId +
                ", text='" + text + '\'' +
                ", sendDate=" + sendDate +
                ", isRead=" + isRead +
                '}';
    }
}
