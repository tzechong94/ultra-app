package ultra.com.server.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ultra.com.server.model.Liked;
import ultra.com.server.model.Post;
import ultra.com.server.model.User;

@Repository
public interface LikeRepo extends JpaRepository<Liked, Long> {

    Optional<Liked> findByPostAndUser(Post post, User currentUser);

    void deleteByPostAndUser(Post post, User currentUser);

    Integer countByPostAndUser(Post post, User currentUser);

    Integer countByPost(Post post);

    boolean existsByPostAndUser(Post post, User currentUser);
    
}
