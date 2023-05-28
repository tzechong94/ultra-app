package ultra.com.server.service;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import ultra.com.server.dto.AuthenticationResponse;
import ultra.com.server.dto.LoginRequest;
import ultra.com.server.dto.RefreshTokenRequest;
import ultra.com.server.dto.RegisterRequest;
import ultra.com.server.exception.UltraException;
import ultra.com.server.model.NotificationEmail;
import ultra.com.server.model.User;
import ultra.com.server.model.VerificationToken;
import ultra.com.server.repo.UserRepo;
import ultra.com.server.repo.VerificationTokenRepo;
import ultra.com.server.security.JwtProvider;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
    
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenRepo verificationTokenRepo;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepo.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Ultra, " +
                "please click on the below url to activate your account : " +
                "http://backend.tryultra.xyz/api/auth/accountVerification/" + token));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepo.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UltraException("User not found with name - " + username));
        user.setEnabled(true);
        userRepo.save(user);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepo.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepo.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new UltraException("Invalid Token")));
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        System.out.println("BEFORE AUTHENTICATE");
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        System.out.println("AFTER AUTHENTICATE");
        synchronized (SecurityContextHolder.class) {

        synchronized (SecurityContextHolder.class) {
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // System.out.println(userDetails.toString());
        System.out.println(authentication);
        }
        String token = jwtProvider.generateToken(authenticate);
        String username = loginRequest.getUsername();
        String refreshToken = refreshTokenService.generateRefreshToken().getToken();
        Instant expiresAt = Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis());

        // this.localStorage.store('authenticationToken', data.authenticationToken)
        // this.localStorage.store('username', data.username)
        // this.localStorage.store('refreshToken', data.refreshToken)
        // this.localStorage.store('expiresAt', data.expiresAt)


        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshToken)
                .expiresAt(expiresAt)
                .username(username)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        synchronized (SecurityContextHolder.class) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication);
            return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
        }
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + username));
    }

}