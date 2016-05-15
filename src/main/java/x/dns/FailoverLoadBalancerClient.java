package x.dns;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FailoverLoadBalancerClient implements LoadBalancerClient {

    private final DnsSrvDiscoveryClient discoveryClient;
    private final Blacklist blacklist;

    public FailoverLoadBalancerClient(DnsSrvDiscoveryClient discoveryClient, Blacklist blacklist) {
        this.discoveryClient = discoveryClient;
        this.blacklist = blacklist;
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        return discoveryClient.getInstances(serviceId)
                .stream()
                .filter(blacklist::isAvailable)
                .findFirst()
                .orElse(null);
    }

    @Override
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId)
                .stream()
                .sorted(this::available)
                .collect(Collectors.toList());

        Exception lastFail = null;
        for (ServiceInstance instance : instances) {
            try {
                // why the T in spring's interface?
                System.out.println("trying " + instance);
                ClientHttpResponse response = (ClientHttpResponse) request.apply(instance);
                if (response.getStatusCode().is2xxSuccessful()) {
                    return (T) response;
                }
            } catch (Exception e) {
                System.err.println("Ignoring " + e);
                lastFail = e;
                blacklist.recordFailure(instance);
            }
        }

        if (lastFail == null) {
            throw new IOException("No instances for " + serviceId);
        }
        throw new IOException("Failed for " + serviceId, lastFail);
    }

    @Override
    public URI reconstructURI(ServiceInstance instance, URI original) {
        // TODO: need to respect path and other bits from original
        return instance.getUri();
    }

    private int available(ServiceInstance a, ServiceInstance b) {
        return Boolean.compare(blacklist.isAvailable(a), blacklist.isAvailable(b));
    }
}
