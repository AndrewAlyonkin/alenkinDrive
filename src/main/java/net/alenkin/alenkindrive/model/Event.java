package edu.alenkin.model;

import com.google.gson.annotations.Expose;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 *
 * Represents the {@link StoredFile} download history for {@link User}
 */

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "events")
public class Event extends BaseEntity {
    @Expose
    @OneToOne
    @JoinColumn(name = "file_id")
    private StoredFile storedFile;

    @Expose
    @CreationTimestamp
    @Column(name = "download_time", columnDefinition = "TIMESTAMP")
    private Timestamp downloadDateTime;

    @ManyToOne
    private User user;

    public Event(StoredFile storedFile) {
        this.storedFile = storedFile;
    }
    public Event(Event event){
        this(event.getId(), event.getDownloadDateTime(), event.getStoredFile(), event.getUser());
    }

    public Event(Long eventId, Timestamp downloadDateTime, StoredFile file, User user) {
        super(eventId);
        this.downloadDateTime = downloadDateTime;
        this.storedFile = file;
        this.user = user;
    }
    public Event(Timestamp downloadDateTime, StoredFile file, User user) {
       this(null, downloadDateTime, file, user);
    }
}
