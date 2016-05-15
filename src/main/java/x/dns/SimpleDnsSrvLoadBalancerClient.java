package x.dns;

import com.spotify.dns.DnsSrvResolver;
import com.spotify.dns.DnsSrvResolvers;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;

import java.net.URI;
import java.net.UnknownHostException;

public class SimpleDnsSrvLoadBalancerClient implements LoadBalancerClient {

    private final DnsSrvResolver resolver;

    public SimpleDnsSrvLoadBalancerClient() {
        resolver = DnsSrvResolvers.newBuilder().build();
        try {
            SimpleResolver defaultResolver = new SimpleResolver("localhost");
            Lookup.setDefaultResolver(defaultResolver);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        System.out.println("resolving " + serviceId);
        return resolver.resolve(serviceId)
                .stream()
                .map(lookupResult -> new DnsSrvServiceInstance(serviceId, lookupResult.host(), lookupResult.port()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request) {
        ServiceInstance serviceInstance = choose(serviceId);
        System.out.println("executing for " + serviceInstance);
        try {
            return request.apply(serviceInstance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public URI reconstructURI(ServiceInstance instance, URI original) {
        return instance.getUri();
    }
}
