package pl.kantoch.dawid.magit.services.file_service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.kantoch.dawid.magit.TaskResult;
import pl.kantoch.dawid.magit.models.MemoryDirectory;
import pl.kantoch.dawid.magit.models.Task;
import pl.kantoch.dawid.magit.models.TaskAttachment;
import pl.kantoch.dawid.magit.repositories.MemoryDirectoriesRepository;
import pl.kantoch.dawid.magit.repositories.TaskAttachmentRepository;
import pl.kantoch.dawid.magit.repositories.TasksRepository;
import pl.kantoch.dawid.magit.repositories.TasksResultsRepository;
import pl.kantoch.dawid.magit.security.JWTUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FilesStorageServiceImpl implements FilesStorageService
{
    private Path root = Paths.get("uploads");

    private final TasksRepository tasksRepository;
    private final TasksResultsRepository tasksResultsRepository;
    private final MemoryDirectoriesRepository memoryDirectoriesRepository;
    private final JWTUtils jwtUtils;
    private final TaskAttachmentRepository taskAttachmentRepository;

    public FilesStorageServiceImpl(TasksRepository tasksRepository,
                                   TasksResultsRepository tasksResultsRepository,
                                   MemoryDirectoriesRepository memoryDirectoriesRepository,
                                   JWTUtils jwtUtils,
                                   TaskAttachmentRepository taskAttachmentRepository) {
        this.tasksRepository = tasksRepository;
        this.tasksResultsRepository = tasksResultsRepository;
        this.memoryDirectoriesRepository = memoryDirectoriesRepository;
        this.jwtUtils = jwtUtils;
        this.taskAttachmentRepository = taskAttachmentRepository;
    }

    @Transactional
    @Override
    public void save(MultipartFile file,Long id,String token) {
        try {
            Optional<Task> optionalTask = tasksRepository.findByIdAndDeletedFalse(id);
            if(optionalTask.isEmpty()) throw new Exception("Nie znaleziono zadania o ID="+id);
            if(optionalTask.get().getProject()==null) throw new Exception("To zadanie nie posiada zdefiniowanego projektu i nie można przesyłać do niego zasobów");
            if(optionalTask.get().getProject()!=null && optionalTask.get().getProject().getDriveName()==null) throw new Exception("Dla projektu tego zadania nie zdefiniowano dysku sieciowego!");
            Optional<MemoryDirectory> memoryDirectoryOptional = memoryDirectoriesRepository.findByProject_Id(optionalTask.get().getProject().getId());
            if(memoryDirectoryOptional.isEmpty()) throw new FileNotFoundException("Nie znaleziono katalogu projektu!");
            File savedFile = new File(memoryDirectoryOptional.get().getGeneratedUrl()+File.separator+file.getOriginalFilename());
            if(savedFile.exists()) throw new Exception(file.getOriginalFilename()+" już istnieje w katalogu docelowym!");
            Files.copy(file.getInputStream(), savedFile.toPath());
            TaskResult taskResult = new TaskResult();
            taskResult.setCreationDate(new Date());
            String username = jwtUtils.getUsernameFromJwtToken(token.substring(7));
            taskResult.setLogin(username);
            taskResult.setResultType(file.getContentType());
            taskResult.setFileUrl(savedFile.getAbsolutePath());
            taskResult.setTaskId(optionalTask.get().getId());
            tasksResultsRepository.save(taskResult);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void saveAttachment(MultipartFile file, Long taskId, String token) {
        try {
            Optional<Task> optionalTask = tasksRepository.findByIdAndDeletedFalse(taskId);
            if(optionalTask.isEmpty()) throw new Exception("Nie znaleziono zadania o ID="+taskId);
            if(optionalTask.get().getProject()==null) throw new Exception("To zadanie nie posiada zdefiniowanego projektu i nie można przesyłać do niego zasobów");
            if(optionalTask.get().getProject()!=null && optionalTask.get().getProject().getDriveName()==null) throw new Exception("Dla projektu tego zadania nie zdefiniowano dysku sieciowego!");
            Optional<MemoryDirectory> memoryDirectoryOptional = memoryDirectoriesRepository.findByProject_Id(optionalTask.get().getProject().getId());
            if(memoryDirectoryOptional.isEmpty()) throw new FileNotFoundException("Nie znaleziono katalogu projektu!");
            File savedFile = new File(memoryDirectoryOptional.get().getGeneratedUrl()+File.separator+file.getOriginalFilename());
            if(savedFile.exists()) throw new Exception(file.getOriginalFilename()+" już istnieje w katalogu docelowym!");
            Files.copy(file.getInputStream(), savedFile.toPath());
            TaskAttachment taskAttachment = new TaskAttachment();
            taskAttachment.setCreationDate(new Date());
            String username = jwtUtils.getUsernameFromJwtToken(token.substring(7));
            taskAttachment.setLogin(username);
            taskAttachment.setAttachmentType(file.getContentType());
            taskAttachment.setFileUrl(savedFile.getAbsolutePath());
            taskAttachment.setTaskId(optionalTask.get().getId());
            taskAttachmentRepository.save(taskAttachment);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }
    @Override
    public List<Path> loadAllResultsFile(Long id) {
        try {
            Optional<Task> optionalTask = tasksRepository.findByIdAndDeletedFalse(id);
            if(optionalTask.isEmpty()) throw new Exception("Nie znaleziono zadania o ID="+id);
            if(optionalTask.get().getProject()==null) throw new Exception("To zadanie nie posiada zdefiniowanego projektu i nie można przesyłać do niego zasobów");
            if(optionalTask.get().getProject()!=null && optionalTask.get().getProject().getDriveName()==null) throw new Exception("Dla projektu tego zadania nie zdefiniowano dysku sieciowego!");
            List<TaskResult> taskResultList = tasksResultsRepository.findAllByTaskId(optionalTask.get().getId());
            List<Path> paths = new ArrayList<>();
            taskResultList.forEach(e->{
                if(e.getFileUrl()!=null){
                    paths.add(Path.of(e.getFileUrl()));
                }
            });
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Nie udało się pobrać plików rezultatowych! "+e.getMessage());
        }
    }

    @Override
    public List<Path> loadAllAttachments(Long taskId) {
        try {
            Optional<Task> optionalTask = tasksRepository.findByIdAndDeletedFalse(taskId);
            if(optionalTask.isEmpty()) throw new Exception("Nie znaleziono zadania o ID="+taskId);
            if(optionalTask.get().getProject()==null) throw new Exception("To zadanie nie posiada zdefiniowanego projektu i nie można przesyłać do niego zasobów");
            if(optionalTask.get().getProject()!=null && optionalTask.get().getProject().getDriveName()==null) throw new Exception("Dla projektu tego zadania nie zdefiniowano dysku sieciowego!");
            List<TaskAttachment> taskAttachmentList = taskAttachmentRepository.findAllByTaskId(optionalTask.get().getId());
            List<Path> paths = new ArrayList<>();
            taskAttachmentList.forEach(e->{
                if(e.getFileUrl()!=null){
                    paths.add(Path.of(e.getFileUrl()));
                }
            });
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Nie udało się pobrać plików załączników! "+e.getMessage());
        }
    }
}
