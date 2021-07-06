package net.alenkin.alenkindrive.service;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.alenkin.alenkindrive.model.Event;
import net.alenkin.alenkindrive.model.StoredFile;
import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.repository.StoredFileRepository;
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
public class StorageFileServiceImpl implements StoredFileService {

    private final StoredFileRepository repository;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    public StorageFileServiceImpl(StoredFileRepository repository) {
        this.repository = repository;
    }

    public StoredFile create(@NotNull StoredFile file) {
        log.info("Create new file");
        return repository.save(file);
    }

    public StoredFile update(@NotNull StoredFile file) {
        long fileId = file.getId();
        log.info("Update file id = {}", fileId);
        return checkNotFoundWithId(repository.save(file), fileId);
    }

    public StoredFile get(Long id, Long userId) {
        log.info("Get file id = {}", id);
        StoredFile file = repository.getByIdAndUserId(id, userId);
        if (file != null) {

            //TODO: Set current user from security context
            User user = userService.get(userId);

            Event downloadEvent = new Event(file, user);
            eventService.create(downloadEvent);
        }
        return file;
    }

    public List<StoredFile> getAllByUserId(long userId) {
        log.info("Get all files");
        return repository.getAllByUserId(userId);
    }

    public void delete(long id, Long userId) {
        log.info("delete file id = {}", id);
        checkNotFoundWithId(repository.deleteByIdAndUserId(id, userId) != 0, id);
    }
}
