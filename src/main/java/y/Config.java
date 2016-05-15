package y;

import com.spotify.dns.DnsSrvResolver;
import com.spotify.dns.DnsSrvResolvers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import x.dns.Blacklist;
import x.dns.DnsSrvDiscoveryClient;
import x.dns.FailoverLoadBalancerClient;
import x.dns.SimpleDnsSrvLoadBalancerClient;

@Configuration
public class Config {
    @Bean
    public SimpleDnsSrvLoadBalancerClient dnsSrvLoadBalancerClient() {
        return new SimpleDnsSrvLoadBalancerClient();
    }

    @ConditionalOnProperty(value = "failover", havingValue = "true")
    @Configuration
    public static class Failover {

        @Primary
        @Bean
        public FailoverLoadBalancerClient failoverClient() {
            return new FailoverLoadBalancerClient(dnsSrvDiscoveryClient(), blacklist());
        }

        @Bean
        public DnsSrvDiscoveryClient dnsSrvDiscoveryClient() {
            return new DnsSrvDiscoveryClient(dnsSrvResolver());
        }

        @Bean
        public DnsSrvResolver dnsSrvResolver() {
            return DnsSrvResolvers.newBuilder()
                    .dnsLookupTimeoutMillis(3000)
                    .retainingDataOnFailures(true)
                    .build();
        }

        @Bean
        public Blacklist blacklist() {
            return new Blacklist();
        }
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
