package pl.kantoch.dawid.magit.services.file_service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FilesStorageService {
    void save(MultipartFile file,Long id,String token);
    Resource load(String filename);
    void deleteAll();
    List<Path> loadAllResultsFile(Long taskId);
    List<Path> loadAllAttachments(Long taskId);
    void saveAttachment(MultipartFile file, Long taskId, String token);
}
