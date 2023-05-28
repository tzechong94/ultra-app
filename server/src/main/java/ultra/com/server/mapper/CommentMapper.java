package ultra.com.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import ultra.com.server.dto.CommentDto;
import ultra.com.server.model.Comment;
import ultra.com.server.model.Post;
import ultra.com.server.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source ="user")
    Comment map(CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "duration", expression = "java(getDuration(comment))")
    CommentDto mapToDto(Comment comment);

    default String getDuration(Comment comment) {
        return TimeAgo.using(comment.getCreatedDate().toEpochMilli());
    }

}
