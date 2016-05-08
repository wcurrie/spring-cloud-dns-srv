package z;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

/**
 * Uncomment the line in src/main/resources/spring/META-INF/spring.factories to try this
 */
@SpringBootApplication
@RestController
public class PropertyDemoApp {

    @Value("${web.service.local}")
    private String serviceUrl;

    @RequestMapping("/")
    public String get() {
        return format("resolved %s%n", serviceUrl);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(PropertyDemoApp.class).web(true).run(args);
    }

}
