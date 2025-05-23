package docker.management.docker_management.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import docker.management.docker_management.dto.GoogleAuthResponse;
import docker.management.docker_management.entity.GoogleUser;
import docker.management.docker_management.exception.AuthenticationException;
import docker.management.docker_management.repo.GoogleUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    private final GoogleUserRepository googleUserRepository;
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String GOOGLE_ISSUER = "accounts.google.com";
    private static final String GOOGLE_ISSUER_HTTPS = "https://accounts.google.com";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    public GoogleAuthResponse authenticateGoogleToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new AuthenticationException("Token cannot be empty");
            }

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(clientId))
                    .setIssuers(Arrays.asList(GOOGLE_ISSUER, GOOGLE_ISSUER_HTTPS))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken == null) {
                log.error("Failed to verify token: {}", token);
                throw new AuthenticationException("Invalid ID token");
            }

            Payload payload = idToken.getPayload();

            // Verify email is verified
            if (!payload.getEmailVerified()) {
                throw new AuthenticationException("Email not verified");
            }

            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            GoogleUser user = googleUserRepository.findByGoogleId(googleId)
                    .orElseGet(() -> {
                        GoogleUser newUser = new GoogleUser();
                        newUser.setGoogleId(googleId);
                        newUser.setEmail(email);
                        newUser.setName(name);
                        newUser.setPicture(picture);
                        return googleUserRepository.save(newUser);
                    });

            return GoogleAuthResponse.builder()
                    .id(user.getGoogleId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .picture(user.getPicture())
                    .message("Authentication successful")
                    .build();
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Token verification failed", e);
            throw new AuthenticationException("Token verification failed: " + e.getMessage(), e);
        }
    }
} 