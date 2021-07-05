package edu.alenkin.model;

import com.google.gson.annotations.Expose;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 *
 * Represents the owner of {@link StoredFile} and {@link Event}
 */

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Expose
    @Column(name = "name", columnDefinition = "VARCHAR")
    private String name;

    @Expose
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<StoredFile> storedFiles;

    @Expose
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Event> events;

    public User(String name, Long id) {
        super(id);
        this.name = name;
    }
    public User(String name) {
        this(name, null);
    }

    public User(Long id, String name, List<StoredFile> storedFiles, List<Event> events) {
        super(id);
        this.name = name;
        this.storedFiles = storedFiles;
        this.events = events;
    }

    public User(User user ) {
        this(user.getId(), user.getName(), user.getStoredFiles(), user.getEvents());
    }

    public void addEvent(Event event) {
        if (!events.contains(event)) {
            this.events.add(event);
        }
    }

    public void addFile(StoredFile file) {
        this.storedFiles.add(file);
    }


}
