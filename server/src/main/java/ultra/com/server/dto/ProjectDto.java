package ultra.com.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ultra.com.server.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long id;

    @NotBlank(message = "Project name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Duration is required")
    private Integer projectDuration;

    private Integer numberOfPosts;
    private User user;
    private String duration;
    private Boolean completed;

}


