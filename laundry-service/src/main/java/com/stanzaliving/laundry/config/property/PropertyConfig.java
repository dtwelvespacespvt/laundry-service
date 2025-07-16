package com.stanzaliving.laundry.config.property;

import com.stanzaliving.laundry.service.property.PropertySourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import static com.stanzaliving.laundry.constant.PropertyConstant.CUSTOM_PROPERTY_SOURCE;


@Configuration
@RequiredArgsConstructor
public class PropertyConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final PropertySourceService propertySourceService;
    private final ConfigurableEnvironment environment;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        propertySourceService.syncRedisProperty();
        CustomPropertySource customPropertySource = new CustomPropertySource(CUSTOM_PROPERTY_SOURCE, propertySourceService);
        environment.getPropertySources().addFirst(customPropertySource);
    }
}
