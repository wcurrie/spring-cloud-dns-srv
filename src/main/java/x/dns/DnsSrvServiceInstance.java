package x.dns;

import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;

import static java.lang.String.format;

public class DnsSrvServiceInstance implements ServiceInstance {

    private final String serviceId;
    private final String host;
    private final int port;
    private final URI field;

    public DnsSrvServiceInstance(String serviceId, String host, int port) {
        this.serviceId = serviceId;
        this.host = host.replaceFirst("\\.$", "");
        this.port = port;
        this.field = URI.create(format("https://%s:%s", this.host, port));
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public URI getUri() {
        return field;
    }

    @Override
    public String toString() {
        return "DnsSrvServiceInstance{" +
                "serviceId='" + serviceId + '\'' +
                ", field=" + field +
                '}';
    }
}
