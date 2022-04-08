package groupd.quiz.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * adds cors mappings and sets the allowed origin
     *
     * @param registry gets inserted automatically by spring
     **/
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*://*");
    }

    /**
     * sets up the folder at the server for storing the profile pictures
     *
     * @param registry gets inserted automatically by spring
     */

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/user-photos/**")
                .addResourceLocations("/opt/user-photos/**", "file:/opt/user-photos/");
    }
}
