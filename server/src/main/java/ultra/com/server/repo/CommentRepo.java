package ultra.com.server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ultra.com.server.model.Comment;
import ultra.com.server.model.Post;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPost(Post post);

}
