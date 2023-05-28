package ultra.com.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ultra.com.server.dto.ProjectDto;
import ultra.com.server.dto.ProjectEditDto;
import ultra.com.server.dto.ProjectResponse;
import ultra.com.server.exception.ProjectNotFoundException;
import ultra.com.server.exception.UltraException;
import ultra.com.server.mapper.ProjectMapper;
import ultra.com.server.model.Project;
import ultra.com.server.model.User;
import ultra.com.server.repo.ProjectRepo;
import ultra.com.server.repo.UserRepo;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;
    private final AuthService authService;
    private final UserRepo userRepo;

    @Transactional
    public ProjectResponse save(ProjectDto projectDto) {
        Project save = projectRepo.save(projectMapper.map(projectDto, authService.getCurrentUser()));
        return projectMapper.mapToDto(save);
    }


    public List<ProjectResponse> getAll() {
        return projectRepo.findAll()
                    .stream()
                    .map(projectMapper::mapToDto)
                    .collect(Collectors.toList());
    }

    public List<ProjectResponse> getProjectsByUserid(Long id) {
        User user = userRepo.findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException(id.toString()));
        return projectRepo.findByUser(user)
                    .stream()
                    .map(projectMapper::mapToDto)
                    .collect(Collectors.toList());
    }

    public ProjectResponse getProject(Long id) {
        Project project = projectRepo.findById(id)
                        .orElseThrow(()-> new UltraException("No project found"));
        System.out.println("PROJECT " + project);
        return projectMapper.mapToDto(project);
    }


    public ProjectResponse updateProject(Long id, ProjectEditDto projectEditDto) {
        Project existingProject = projectRepo.findById(id)
                                    .orElseThrow(() -> new UltraException("No project found"));

        existingProject.setName(projectEditDto.getName());
        existingProject.setDescription(projectEditDto.getDescription());
        existingProject.setProjectDuration(projectEditDto.getProjectDuration());
        existingProject.setCompleted(projectEditDto.getCompleted());
        Project save = projectRepo.save(existingProject);

        return projectMapper.mapToDto(save);
    }


    public void deleteProject(Long id) {

        Project project = projectRepo.findById(id)
            .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
    
        projectRepo.delete(project);
        }

    
}
