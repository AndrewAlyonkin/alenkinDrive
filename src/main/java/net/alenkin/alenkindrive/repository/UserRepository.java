package net.alenkin.alenkindrive.repository;

import net.alenkin.alenkindrive.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
