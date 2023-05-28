package ultra.com.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import ultra.com.server.dto.UserDto;
import ultra.com.server.model.User;
import ultra.com.server.service.ProjectService;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    
    @Autowired 
    private ProjectService projectService;

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "projectCount", expression = "java(projectCount(user))")
    public abstract UserDto mapToDto(User user);


    Integer projectCount(User user) {
        return projectService.getProjectsByUserid(user.getUserId()).size();
    }
}


