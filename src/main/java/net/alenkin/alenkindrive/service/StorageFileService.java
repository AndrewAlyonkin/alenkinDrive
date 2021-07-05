package net.alenkin.alenkindrive.service;

import com.sun.istack.NotNull;
import net.alenkin.alenkindrive.model.StoredFile;
import net.alenkin.alenkindrive.repository.StoredFileRepository;

import java.util.List;

import static net.alenkin.alenkindrive.util.ValidationUtil.checkNotFoundWithId;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public class StorageFileService {
    private StoredFileRepository repository;

    public StoredFile create(@NotNull StoredFile file) {
        return repository.save(file);
    }

    public StoredFile update(@NotNull StoredFile file) {
        return checkNotFoundWithId(repository.save(file), file.getId());
    }

    public StoredFile get(long id) {
        return checkNotFoundWithId(repository.getOne(id), id);
    }

    public List<StoredFile> getAllByUserId(long userId) {
        return repository.getAllByUserId(userId);
    }

    public void delete(long id) {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
