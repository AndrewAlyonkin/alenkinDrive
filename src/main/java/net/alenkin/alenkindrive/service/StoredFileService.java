package net.alenkin.alenkindrive.service;

import com.sun.istack.NotNull;
import net.alenkin.alenkindrive.model.StoredFile;

import java.util.List;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public interface StoredFileService {
    StoredFile create(@NotNull StoredFile file);

    StoredFile update(@NotNull StoredFile file);

    StoredFile get(Long id, Long userId);

    List<StoredFile> getAllByUserId(long userId);

    void delete(long id, Long userId);
}
