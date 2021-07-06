package net.alenkin.alenkindrive.repository;

import net.alenkin.alenkindrive.model.Event;
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
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> getAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Event e WHERE e.id=:id and e.user.id=:userId")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Event getByIdAndUserId(Long id, Long userId);
}
