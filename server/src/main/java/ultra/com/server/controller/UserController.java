package ultra.com.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ultra.com.server.dto.UserDto;
import ultra.com.server.service.UserService;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@CrossOrigin("*")
public class UserController {
    
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getAllProjectsByUserid(@PathVariable String username) {
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.getUser(username));
    }

}


