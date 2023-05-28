package ultra.com.server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ultra.com.server.dto.ProjectDto;
import ultra.com.server.dto.ProjectEditDto;
import ultra.com.server.dto.ProjectResponse;
import ultra.com.server.service.ProjectService;

@RestController
@RequestMapping("/api/project")
@AllArgsConstructor
@CrossOrigin("*")
public class ProjectController {
    
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectDto projectDto) {
        System.out.println(projectDto + " projectDTO");
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(projectService.save(projectDto));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ProjectResponse>> getAllProjectsByUserid(@PathVariable Long id) {
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.getProjectsByUserid(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.getProject(id));
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<ProjectResponse> editProject(@PathVariable Long id, 
    @RequestBody ProjectEditDto projectEditDto) {

        return ResponseEntity.status(HttpStatus.OK)
                            .body(projectService.updateProject(id, projectEditDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        
        System.out.println("deleted");
        return ResponseEntity.status(HttpStatus.OK)
                            .body("Project deleted");
    }
}


