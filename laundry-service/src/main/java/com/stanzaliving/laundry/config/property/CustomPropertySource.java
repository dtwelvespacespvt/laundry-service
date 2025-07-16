package com.stanzaliving.laundry.config.property;

import com.stanzaliving.laundry.service.property.PropertySourceService;
import org.springframework.core.env.PropertySource;

import java.util.Objects;

public class CustomPropertySource extends PropertySource<PropertySourceService> {

    public CustomPropertySource(String name, PropertySourceService source) {
        super(name, source);
    }

    @Override
    public Object getProperty(String key) {
        Object valueFromRedis = source.getPropertyFromRedis(key);
        if (Objects.nonNull(valueFromRedis)) {
            return valueFromRedis;
        }
        Object valueFromDB = source.getPropertyFromDB(key);
        if (Objects.nonNull(valueFromDB)) {
            return valueFromDB;
        }
        return null;
    }
}
