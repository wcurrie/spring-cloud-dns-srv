package y;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import x.dns.DnsSrvLoadBalancerClient;

@Configuration
public class Config {
    @Bean
    public DnsSrvLoadBalancerClient dnsSrvLoadBalancerClient() {
        return new DnsSrvLoadBalancerClient();
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
