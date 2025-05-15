package docker.management.docker_management.controller;

import docker.management.docker_management.dto.DockerCommandRequest;
import docker.management.docker_management.service.DockerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docker")
public class DockerManagementController {

    @Autowired
    private DockerManagementService dockerService;

    @PostMapping("/run")
    public ResponseEntity<String> runContainer(@RequestBody DockerCommandRequest request) {
        String result = dockerService.runContainer(request.getImage());
        return ResponseEntity.ok(result);
    }
}

