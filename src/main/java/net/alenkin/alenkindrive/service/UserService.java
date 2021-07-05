package net.alenkin.alenkindrive.service;

import lombok.NonNull;
import net.alenkin.alenkindrive.model.User;

import java.util.List;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public interface UserService {
    User create(@NonNull User user);

    User get(long id);

    List<User> getAll();

    User update(@NonNull User user);

    void delete(long id);
}
