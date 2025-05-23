package docker.management.docker_management.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "google_users")
public class GoogleUser {
    @Id
    @Column(name = "google_id")
    private String googleId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private String picture;
} 