package docker.management.docker_management.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleAuthResponse {
    private String id;
    private String email;
    private String name;
    private String picture;
    private String message;
} 