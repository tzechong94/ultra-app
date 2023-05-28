package ultra.com.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ultra.com.server.dto.CommentDto;
import ultra.com.server.exception.PostNotFoundException;
import ultra.com.server.mapper.CommentMapper;
import ultra.com.server.model.Comment;
import ultra.com.server.model.NotificationEmail;
import ultra.com.server.model.Post;
// import ultra.com.server.model.User;
import ultra.com.server.repo.CommentRepo;
import ultra.com.server.repo.PostRepo;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {

    private final PostRepo postRepo;
    private final AuthService authService;
    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;
    private final MailService mailService;
    // private final MailContentBuilder mailContentBuilder;

    public CommentDto save(CommentDto commentDto) {
        Post post = postRepo.findById(commentDto.getPostId())
            .orElseThrow(()-> new PostNotFoundException(commentDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentDto, post, authService.getCurrentUser());
        commentRepo.save(comment);

        // String message = mailContentBuilder.build(comment.getUser().getUsername() + " posted a comment on your post: "
        //  + post.getPostName());

        mailService.sendMail((new NotificationEmail("New Comment on your post",
        post.getUser().getEmail(), "New comment by " + comment.getUser().getUsername() + " on your post: "
        + post.getPostName()
        )));
        // sendCommentNotification(message, post.getUser(), comment);
        return commentDto;
    }

    // private void sendCommentNotification(String message, User user, Comment comment) {
    //     mailService.sendMail(new NotificationEmail(comment.getUser() + " Commented on your post", user.getEmail(), message));
    // }

    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new PostNotFoundException(postId.toString()));
        return commentRepo.findByPost(post)
                    .stream()
                    .map(commentMapper::mapToDto)
                    .toList();
    }
    
}
