package ultra.com.server.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ultra.com.server.exception.UltraException;
import ultra.com.server.model.RefreshToken;
import ultra.com.server.repo.RefreshTokenRepo;


@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;


    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepo.save(refreshToken);
    }

    void validateRefreshToken(String token) {
        refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new UltraException("Invalid refresh token"));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepo.deleteByToken(token);
    }

    
    

}
