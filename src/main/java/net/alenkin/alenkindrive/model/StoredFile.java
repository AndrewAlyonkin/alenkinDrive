package net.alenkin.alenkindrive.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 * <p>
 * Contains information about stored file and it URI in storage (server file system)
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "files")
public class StoredFile extends BaseEntity {
    @Column(name = "file_uri", columnDefinition = "VARCHAR")
    private String fileURI;

    @Column(name = "size", columnDefinition = "BIGINT")
    private long size;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public StoredFile(Long id, String fileURI, long size, User user) {
        super(id);
        this.fileURI = fileURI;
        this.size = size;
        this.user = user;
    }

    public StoredFile(String fileURI, long size, User user) {
        super();
        this.fileURI = fileURI;
        this.size = size;
        this.user = user;
    }
}
