package y;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@SpringBootApplication
@ComponentScan
@RestController
public class Application {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @RequestMapping("/")
    public String get() {
        ResponseEntity<String> entity = restTemplate.getForEntity("https://web.service.local", String.class);
        return format("urls %s%n", entity.getBody());
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

}
