package ultra.com.server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ultra.com.server.model.Project;
import ultra.com.server.model.Post;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    
    List<Post> findAllByProject(Project project);

}
