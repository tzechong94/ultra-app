package ultra.com.server.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long commentId;
    private Long postId;

    private Instant createdDate;

    @NotBlank(message = "Comment text cannot be blank")
    private String text;
    private String username;
    private String duration;
}
