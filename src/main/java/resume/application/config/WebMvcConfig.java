package resume.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("css/**").addResourceLocations("classpath:/webapp/css/");
        registry.addResourceHandler("template/**").addResourceLocations("classpath:/webapp/template/");
        registry.addResourceHandler("js/**").addResourceLocations("classpath:/webapp/js/");
        registry.addResourceHandler("view/**").addResourceLocations("classpath:/webapp/view/");
    }

    @Bean
    public ViewResolver viewResolver()
    {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("view/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
