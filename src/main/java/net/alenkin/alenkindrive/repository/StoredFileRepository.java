package net.alenkin.alenkindrive.repository;

import net.alenkin.alenkindrive.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {
    List<StoredFile> getAllByUserId(Long userId);
}
