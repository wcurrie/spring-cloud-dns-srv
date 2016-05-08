package y;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RegisterDnsSrvPropertySource implements ApplicationContextInitializer {

    public RegisterDnsSrvPropertySource() {
        System.out.println("new RegisterDnsSrvPropertySource");
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("adding dns resolver");
        DnsSrvRecordPropertySource propertySource = new DnsSrvRecordPropertySource();
        applicationContext.getEnvironment().getPropertySources().addLast(propertySource);
    }
}
