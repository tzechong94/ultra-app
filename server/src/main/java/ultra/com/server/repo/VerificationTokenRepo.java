package ultra.com.server.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ultra.com.server.model.VerificationToken;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long>  {
    Optional<VerificationToken> findByToken(String token);
}
