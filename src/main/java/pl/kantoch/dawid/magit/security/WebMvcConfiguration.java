package pl.kantoch.dawid.magit.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    private final UserLogDataInterceptor userLogDataInterceptor;

    public WebMvcConfiguration(UserLogDataInterceptor userLogDataInterceptor) {
        this.userLogDataInterceptor = userLogDataInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLogDataInterceptor);
    }
}
