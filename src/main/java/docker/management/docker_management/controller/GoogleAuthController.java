package docker.management.docker_management.controller;

import docker.management.docker_management.dto.GoogleAuthRequest;
import docker.management.docker_management.dto.GoogleAuthResponse;
import docker.management.docker_management.exception.AuthenticationException;
import docker.management.docker_management.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GoogleAuthController {
    private final GoogleAuthService googleAuthService;

    @PostMapping("/google")
    public ResponseEntity<GoogleAuthResponse> authenticateGoogle(@RequestBody GoogleAuthRequest request) {
        try {
            if (request == null || request.getToken() == null || request.getToken().trim().isEmpty()) {
                throw new AuthenticationException("Token is required");
            }

            GoogleAuthResponse response = googleAuthService.authenticateGoogleToken(request.getToken());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during authentication", e);
            throw new AuthenticationException("Authentication failed: " + e.getMessage(), e);
        }
    }
} 