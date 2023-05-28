package ultra.com.server.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import ultra.com.server.dto.ProjectDto;
import ultra.com.server.dto.ProjectResponse;
import ultra.com.server.model.Project;
import ultra.com.server.model.User;
import ultra.com.server.repo.PostRepo;

@Mapper(componentModel = "spring")
public abstract class ProjectMapper {

    @Autowired
    private PostRepo postRepo;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "posts", ignore = true)
    public abstract Project map(ProjectDto projectDto, User user);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "postCount", expression = "java(postCount(project))")
    @Mapping(target = "duration", expression = "java(getDuration(project))")
    public abstract ProjectResponse mapToDto(Project project);

    Integer postCount(Project project) {
        return postRepo.findAllByProject(project).size();
    }

    String getDuration(Project project) {
        return TimeAgo.using(project.getCreatedDate().toEpochMilli());
    }
        
    

}
