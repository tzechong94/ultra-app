package ultra.com.server.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private String username;
    private Integer projectDuration;
    private Integer postCount;
    private Instant createdDate;
    private String duration;
    private Boolean completed;

}


