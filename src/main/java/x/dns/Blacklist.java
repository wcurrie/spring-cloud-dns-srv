package x.dns;

import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Blacklist {

    private Map<URI, Long> unavailable = new ConcurrentHashMap<>();

    public boolean isAvailable(ServiceInstance instance) {
        URI uri = instance.getUri();
        return !unavailable.containsKey(uri) || unavailable.get(uri) > System.currentTimeMillis();
    }

    public void recordFailure(ServiceInstance instance) {
        System.out.println("Banning " + instance.getUri() + " for 30 seconds");
        unavailable.put(instance.getUri(), System.currentTimeMillis() + 30000);
    }
}
