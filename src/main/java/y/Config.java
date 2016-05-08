package y;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import x.dns.DnsSrvLoadBalancerClient;

@Configuration
public class Config {
    @Bean
    public DnsSrvLoadBalancerClient dnsSrvLoadBalancerClient() {
        return new DnsSrvLoadBalancerClient();
    }
}
