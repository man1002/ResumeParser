package resume.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import resume.parser.aspect.ValidateNotNull;

@Configuration
@EnableAspectJAutoProxy
public class AspectJConfig {
    @Bean
    public ValidateNotNull validate() {
        return new ValidateNotNull();
    }
}
