package ultra.com.server.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ultra.com.server.dto.UserDto;
import ultra.com.server.exception.UltraException;
import ultra.com.server.mapper.UserMapper;
import ultra.com.server.model.User;
import ultra.com.server.repo.UserRepo;

@Service
@AllArgsConstructor
public class UserService {
    
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserDto getUser(String username) {
        User user = userRepo.findByUsername(username)
                        .orElseThrow(()-> new UltraException("No user found"));
        return userMapper.mapToDto(user);
    }

}
