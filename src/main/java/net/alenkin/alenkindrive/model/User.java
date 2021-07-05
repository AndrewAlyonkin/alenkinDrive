package net.alenkin.alenkindrive.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 * <p>
 * Represents the owner of {@link StoredFile} and {@link Event}
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR")
    private String name;

    public User(Long id, String name) {
        super(id);
        this.name = name;
    }

    public User(String name) {
        this(null, name);
    }

    public User(User user) {
        this(user.getId(), user.getName());
    }

}
