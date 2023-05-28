package ultra.com.server.mapper;

// import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import ultra.com.server.dto.PostRequest;
import ultra.com.server.dto.PostResponse;
// import ultra.com.server.model.Liked;
import ultra.com.server.model.Post;
import ultra.com.server.model.Project;
import ultra.com.server.model.User;
import ultra.com.server.repo.CommentRepo;
import ultra.com.server.repo.LikeRepo;
import ultra.com.server.service.AuthService;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    
    @Autowired
    private AuthService authService;
    @Autowired
    private LikeRepo likeRepo;
    @Autowired
    private CommentRepo commentRepo;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "likeCount", constant = "0")
    @Mapping(target = "project", source = "project")
    @Mapping(target = "postReflections", source = "postRequest.postReflections")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "mediaUrl", source = "postRequest.mediaUrl")
    @Mapping(target = "commentList", ignore = true)
    @Mapping(target = "likedList", ignore = true)
    public abstract Post map(PostRequest postRequest, Project project, User user);

    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "id", source = "project.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "postLiked", expression = "java(postLiked(post))")
    @Mapping(target = "name", source = "project.name")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "mediaUrl", source = "mediaUrl")
    @Mapping(target = "postLocation", source = "postLocation")
    @Mapping(target = "currentUserLiked", expression = "java(postLiked(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepo.findByPost(post).size();
    }

    boolean postLiked(Post post) {
        if (authService.isLoggedIn()) {
            // Optional<Liked> liked = likeRepo.findByPostAndUser(post, authService.getCurrentUser());
            boolean currentUserLiked = likeRepo.existsByPostAndUser(post, authService.getCurrentUser());
            System.out.println("POSTLIKED " + currentUserLiked);
            return currentUserLiked;
        }
        return false;
    }

    // private Boolean hasUserLikedPost(Post post) {
    //     if (authService.isLoggedIn()) {
    //         // Optional<Liked> liked = likeRepo.findByPostAndUser(post, authService.getCurrentUser());
    //         Boolean currentUserLiked = likeRepo.existsByPostAndUser(post, authService.getCurrentUser());
    //         System.out.println("POSTLIKED" + currentUserLiked);
    //         return currentUserLiked;
    //     }
    //     return false;
    // }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

}
