package com.stanzaliving.laundry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanzaliving.secure.SecurityModuleBuilderV1;
import com.stanzaliving.secure.interceptor.AuthInterceptorV1;
import com.stanzaliving.secure.interceptor.UIDInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.*;


/**c
 * @author naveen.kumar
 *
 * @date 30-Oct-2019
 *
 **/
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${enable.cors:false}")
    private Boolean enableCors;

    @Value("${service.user.url}")
    private String userServiceUrl;

    @Value("${enable.authentication:false}")
    private Boolean enableAuthentication;

    @Value("${enable.authorize:false}")
    private Boolean enableAuthorization;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "PUT", "POST", "HEAD", "OPTIONS", "DELETE" );
            }
        };
    }

    @Bean
    public UIDInterceptor uidInterceptor() {
        return new UIDInterceptor();
    }

    @Bean
    public AuthInterceptorV1 authInterceptorV1() {
        return new SecurityModuleBuilderV1()
                .objectMapper(objectMapper)
                .corsSupport(enableCors)
                .userServiceUrl(userServiceUrl)
                .enableTokenBasedAuthentication(enableAuthentication)
                .enableUrlBasedAuthorization(enableAuthorization)
                .build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(uidInterceptor())
                .addPathPatterns("/**").order(Ordered.HIGHEST_PRECEDENCE);

        registry.addInterceptor(authInterceptorV1())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/internal/**",
                        "/kafka/**",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/auth/login",
                        "/business/**");

    }
}

