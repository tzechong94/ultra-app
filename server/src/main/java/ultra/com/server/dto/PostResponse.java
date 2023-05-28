package ultra.com.server.dto;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    
    private Long postId;
    private String postName;
    private String postReflections;
    private String username;
    private String name;
    private Long id;
    private Integer likeCount;
    private Integer commentCount;
    // private Instant createdDate;
    private boolean postLiked;
    private String duration;
    private Long userId;
    private String mediaUrl;
    private boolean currentUserLiked;
    private String postLocation;

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("postId", postId)
            .add("postName", postName)
            .add("postReflections", postReflections)
            .add("username", username)
            .add("name", name)
            .add("id", id)
            .add("likeCount", likeCount)
            .add("commentCount", commentCount)
            .add("postLiked", postLiked)
            .add("duration", duration)
            .add("userId", userId)
            .add("mediaUrl", mediaUrl)
            .add("currentUserLiked", currentUserLiked)
            .add("postLocation", postLocation)
            .build();
    }

    public static PostResponse toPostRespose(JsonObject o) {
        PostResponse p = new PostResponse();
        p.setCommentCount(o.getInt("commentCount"));
        p.setCurrentUserLiked(o.getBoolean("currentUserLiked"));
        p.setDuration(o.getString("duration"));
        p.setId(Long.valueOf(o.getInt("id")));
        p.setLikeCount(o.getInt("likeCount"));
        p.setMediaUrl(o.get("mediaUrl").toString());
        p.setName(o.getString("name"));
        p.setPostId(Long.valueOf(o.getInt("postId")));
        p.setPostLiked(o.getBoolean("postLiked"));
        p.setPostName(o.getString("postName"));
        p.setPostReflections(o.getString("postReflections"));
        p.setUserId(Long.valueOf(o.getInt("userId")));
        p.setUsername(o.getString("username"));
        p.setPostLocation(o.getString("postLocation"));
        if (p.getMediaUrl() == "null") {
            p.setMediaUrl(null);
        }
        return p;
    }

}
