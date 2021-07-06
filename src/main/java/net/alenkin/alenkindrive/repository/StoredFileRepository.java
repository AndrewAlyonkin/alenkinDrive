package net.alenkin.alenkindrive.repository;

import net.alenkin.alenkindrive.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@Repository
@Transactional(readOnly = true)
public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {
    List<StoredFile> getAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM StoredFile f WHERE f.id=:id and f.user.id=:userId")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    StoredFile getByIdAndUserId(Long id, Long userId);
}
