package net.alenkin.alenkindrive.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.alenkin.alenkindrive.util.ValidationUtils.checkNotFoundWithId;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public User create(@NonNull User user) {
        log.info("Create new user");
        return repository.save(user);
    }

    public User get(long id) {
        log.info("Get user id = {}", id);
        return repository.findById(id).orElse(null);
    }

    public List<User> getAll() {
        log.info("Get all users");
        return repository.findAll();
    }

    public User update(@NonNull User user) {
        long userId = user.getId();
        log.info("Update user id = {}", userId);
        return checkNotFoundWithId(repository.save(user), userId);
    }

    public void delete(long id) {
        log.info("Delete user id = {}", id);
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public User getGhost(Long userId){
        return repository.getOne(userId);
    }
}
