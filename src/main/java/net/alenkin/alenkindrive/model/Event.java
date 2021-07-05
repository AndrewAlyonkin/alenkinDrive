package net.alenkin.alenkindrive.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 * <p>
 * Represents the {@link StoredFile} download history for {@link User}
 */

@NoArgsConstructor
@Data
@Entity
@Table(name = "events")
public class Event extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "file_id")
    private StoredFile storedFile;

    @CreationTimestamp
    @Column(name = "download_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime downloadDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Event(Long id, StoredFile storedFile, LocalDateTime downloadDateTime, User user) {
        super(id);
        this.storedFile = storedFile;
        this.downloadDateTime = downloadDateTime;
        this.user = user;
    }

    public Event(StoredFile storedFile, LocalDateTime downloadDateTime, User user) {
        super();
        this.storedFile = storedFile;
        this.downloadDateTime = downloadDateTime;
        this.user = user;
    }
}
