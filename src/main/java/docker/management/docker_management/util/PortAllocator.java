package docker.management.docker_management.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;


@Component
public class PortAllocator {
    private final Set<Integer> usedPorts = new HashSet<>();
    private final Set<Integer> blacklist = Set.of(8080);

    public synchronized int getNextAvailablePort(int startPort, int endPort) {
        for (int port = startPort; port <= endPort; port++) {
            if (usedPorts.contains(port) || blacklist.contains(port)) {
                continue;
            }

            if (isPortAvailable(port)) {
                usedPorts.add(port);
                return port;
            }
        }
        throw new RuntimeException("No available ports in the specified range");
    }

    private boolean isPortAvailable(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            socket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
