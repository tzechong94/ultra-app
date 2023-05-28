package ultra.com.server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEditDto {
    
    private Long postId;
    private Long id; // project id

    // @NotBlank(message = "Post name cannot be empty")
    private String postName;

    // private String imageUrl;
    private String postReflections;
    private String mediaUrl;

}
