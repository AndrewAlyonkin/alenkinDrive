package net.alenkin.alenkindrive.repository;

import net.alenkin.alenkindrive.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@Transactional(readOnly = true)
public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {
    List<StoredFile> getAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM files f WHERE f.id=:id")
    Long delete(@Param("id") Long id);
}
