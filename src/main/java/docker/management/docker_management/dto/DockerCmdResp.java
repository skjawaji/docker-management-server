package docker.management.docker_management.dto;

import lombok.Data;

@Data
public class DockerCmdResp {
    private String containerId;
    private String url;
    private String error;
}
