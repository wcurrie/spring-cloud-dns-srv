package y;

import com.spotify.dns.DnsSrvResolver;
import com.spotify.dns.DnsSrvResolvers;
import org.springframework.core.env.EnumerablePropertySource;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;

import java.net.UnknownHostException;
import java.util.stream.Collectors;

public class DnsSrvRecordPropertySource extends EnumerablePropertySource {

    private final DnsSrvResolver resolver;

    public DnsSrvRecordPropertySource() {
        super("dns-srv-records");
        resolver = DnsSrvResolvers.newBuilder().build();
        try {
            SimpleResolver defaultResolver = new SimpleResolver("localhost");
//            defaultResolver.setPort(8600);
            Lookup.setDefaultResolver(defaultResolver);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getPropertyNames() {
        return new String[0];
    }

    public Object getProperty(String name) {
        System.out.println("resolving " + name);
        return resolver.resolve(name)
                .stream()
                .map(lookupResult -> "https://" + lookupResult.host() + ":" + lookupResult.port())
                .collect(Collectors.joining(","));
    }

    public static void main(String[] args) {
        DnsSrvRecordPropertySource source = new DnsSrvRecordPropertySource();
        System.out.println(source.getProperty("web.service.local"));
    }
}
