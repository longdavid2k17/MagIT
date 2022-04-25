package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.MemoryDirectory;
import pl.kantoch.dawid.magit.models.Project;
import pl.kantoch.dawid.magit.repositories.MemoryDirectoriesRepository;

import java.io.File;
import java.util.Date;
import java.util.Locale;

@Service
public class MemoryDirectoriesService {
    private final Logger LOGGER = LoggerFactory.getLogger(MemoryDirectoriesService.class);

    private final MemoryDirectoriesRepository memoryDirectoriesRepository;

    public MemoryDirectoriesService(MemoryDirectoriesRepository memoryDirectoriesRepository) {
        this.memoryDirectoriesRepository = memoryDirectoriesRepository;
    }

    @Transactional
    public boolean createNAS(Project project){
        if(project.getDriveName()==null)
            return false;
        MemoryDirectory directory = new MemoryDirectory();
        directory.setCreationDate(new Date());
        directory.setOrganisation(project.getOrganisation());
        directory.setProject(project);
        String projectName = directory.getOrganisation().getName().toLowerCase(Locale.ROOT).replaceAll("\\s+","");
        directory.setGeneratedUrl(projectName+"/"+project.getDriveName().replaceAll("\\s+",""));
        MemoryDirectory saved;
        try {
            saved = memoryDirectoriesRepository.save(directory);
        }
        catch (Exception e){
            LOGGER.error("Error in MemoryDirectoriesService.createNAS() for project entity = {}. Message: {}", project,e.getMessage());
            return false;
        }
        try {
            File directoryFile = new File(saved.getGeneratedUrl());
            if(directoryFile.exists())
                return false;
            if(directoryFile.mkdirs())
            {
                saved.setGeneratedUrl(directoryFile.getAbsolutePath());
                memoryDirectoriesRepository.save(saved);
                return true;
            }
            else {
                LOGGER.error("Error in MemoryDirectoriesService.createNAS(). Directory does not exist!");
                return false;
            }
        }
        catch (Exception e){
            LOGGER.error("Error in MemoryDirectoriesService.createNAS() during creation directory for project entity = {}. Message: {}", project,e.getMessage());
            return false;
        }
    }
}
