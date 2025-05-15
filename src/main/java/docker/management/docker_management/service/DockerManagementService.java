package docker.management.docker_management.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DockerManagementService {

    private final DockerClient dockerClient;

    @Autowired
    public DockerManagementService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public String runContainer(String imageName) {
        try {
            // Create container
            CreateContainerResponse container = dockerClient.createContainerCmd(imageName).exec();

            // Start container
            dockerClient.startContainerCmd(container.getId()).exec();

            return "Container started with ID: " + container.getId();
        } catch (DockerException e) {
            return "Error: " + e.getMessage();
        }
    }
}
