package docker.management.docker_management.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.RestartPolicy;
import docker.management.docker_management.dto.DockerCmdResp;
import docker.management.docker_management.util.PortAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;

@Service
public class DockerManagementService {

    private final DockerClient dockerClient;
    private final PortAllocator portAllocator;
    @Value("${port.range.start}")
    private int startPort;
    @Value("${port.range.end}")
    private int endPort;
    @Value("${docker.baseUrl}")
    private String baseUrl;

    @Autowired
    public DockerManagementService(DockerClient dockerClient, PortAllocator portAllocator) {
        this.dockerClient = dockerClient;
        this.portAllocator = portAllocator;
    }

    public DockerCmdResp runContainer(String imageName) {
        DockerCmdResp resp = new DockerCmdResp();
        try {
            // Allocate the next available port
            int hostPort = portAllocator.getNextAvailablePort(startPort, endPort);

            // Setup port bindings
            Ports portBindings = new Ports();
            portBindings.bind(ExposedPort.tcp(8080), Ports.Binding.bindPort(hostPort));  // Bind host port
            portBindings.bind(ExposedPort.tcp(9323), Ports.Binding.bindPort(hostPort + 1000));  // Another port

            // Setup host config with restart policy
            HostConfig hostConfig = HostConfig.newHostConfig()
                    .withPortBindings(portBindings)
                    .withRestartPolicy(RestartPolicy.unlessStoppedRestart());

            // Create container with specified configurations
            CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                    .withName(imageName + "-container-" + OffsetDateTime.now().toEpochSecond())
                    .withExposedPorts(ExposedPort.tcp(8080), ExposedPort.tcp(9323))
                    .withHostConfig(hostConfig)
                    .exec();

            // Start container (do this only once)
            dockerClient.startContainerCmd(container.getId()).exec();

            // Set response with container ID and URL for access
            resp.setContainerId(container.getId());
            resp.setUrl(baseUrl + ":" + hostPort + "/?folder=/home/coder/projects");

            return resp;
        } catch (DockerException e) {
            // Log the exception and provide more detailed error feedback
            e.printStackTrace();
            resp.setError("Error while running docker command: " + e.getMessage());
            return resp;
        }
    }

    public String stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId)
                .withTimeout(30)  // Timeout in seconds for graceful stop
                .exec();
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        return "Container Stopped successfully";
    }

}
