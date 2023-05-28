package ultra.com.server.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ultra.com.server.dto.AuthenticationResponse;
import ultra.com.server.dto.LoginRequest;
import ultra.com.server.dto.RefreshTokenRequest;
import ultra.com.server.dto.RegisterRequest;
import ultra.com.server.service.AuthService;
import ultra.com.server.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@AllArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.signup(registerRequest);
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("message", "User Registration is successful");
            JsonObject jsonObject = builder.build();
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>("Username already exist", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<String> isLoggedIn(){
        return null;
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        System.out.println("login request " + loginRequest);
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted.");
    }


}
