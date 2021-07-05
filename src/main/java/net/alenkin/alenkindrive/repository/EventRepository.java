package net.alenkin.alenkindrive.repository;

import net.alenkin.alenkindrive.model.Event;
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
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> getAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM events e WHERE e.id=:id")
    Long delete(@Param("id") Long id);
}
