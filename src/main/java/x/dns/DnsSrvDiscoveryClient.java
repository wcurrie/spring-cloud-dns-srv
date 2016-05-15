package x.dns;

import com.spotify.dns.DnsSrvResolver;
import com.spotify.dns.LookupResult;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DnsSrvDiscoveryClient implements DiscoveryClient {

    private final DnsSrvResolver resolver;

    public DnsSrvDiscoveryClient(DnsSrvResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public String description() {
        return "DNS SRV record Discovery Client";
    }

    @Override
    public ServiceInstance getLocalServiceInstance() {
        return null;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        try {
            return resolver.resolve(serviceId)
                    .stream()
                    .map(lookupResult -> {
                        System.out.println("Resolved " + lookupResult);
                        return toServiceInstance(serviceId, lookupResult);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Ignoring " + e);
            return Collections.emptyList();
        }
    }

    private DnsSrvServiceInstance toServiceInstance(String serviceId, LookupResult lookupResult) {
        return new DnsSrvServiceInstance(serviceId, lookupResult.host(), lookupResult.port());
    }

    @Override
    public List<String> getServices() {
        return null;
    }

}
