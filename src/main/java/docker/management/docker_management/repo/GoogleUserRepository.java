package docker.management.docker_management.repo;

import docker.management.docker_management.entity.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, String> {
    Optional<GoogleUser> findByGoogleId(String googleId);
} 