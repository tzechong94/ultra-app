package ultra.com.server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ultra.com.server.model.Project;
import ultra.com.server.model.User;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

    List<Project> findByUser(User user);
    
}
