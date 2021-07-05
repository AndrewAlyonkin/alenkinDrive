package net.alenkin.alenkindrive.service;

import lombok.NonNull;
import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.repository.UserRepository;
import org.springframework.util.Assert;

import java.util.List;

import static net.alenkin.alenkindrive.util.ValidationUtil.checkNotFoundWithId;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public class UserService {
    private UserRepository repository;

    public User create(@NonNull User user) {
        return repository.save(user);
    }

    public User get(long id) {
        return checkNotFoundWithId(repository.getOne(id), id);
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User update(@NonNull User user) {
        return checkNotFoundWithId(repository.save(user), user.getId());
    }

    public void delete(long id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }
}
