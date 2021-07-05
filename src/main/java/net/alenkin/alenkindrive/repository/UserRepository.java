package net.alenkin.alenkindrive.repository;

import net.alenkin.alenkindrive.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    Long delete(@Param("id") Long id);
}
