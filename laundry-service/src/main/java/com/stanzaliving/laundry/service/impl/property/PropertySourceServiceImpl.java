package com.stanzaliving.laundry.service.impl.property;


import com.stanzaliving.laundry.entity.property.PropertySource;
import com.stanzaliving.laundry.repository.property.PropertySourceRepository;
import com.stanzaliving.laundry.service.property.PropertySourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.stanzaliving.laundry.constant.PropertyConstant.CONFIG_KEY_PREFIX;
import static com.stanzaliving.laundry.constant.PropertyConstant.PROPERTY_SOURCE_REDIS_MAP;

@Log4j2
@Service
@RequiredArgsConstructor
public class PropertySourceServiceImpl implements PropertySourceService {

    private final RedissonClient redissonClient;
    private final PropertySourceRepository laundryPropertySourceRepository;

    @Override
    public Object getPropertyFromRedis(String key) {
        RMap<String, Object> propertyMap = redissonClient.getMap(PROPERTY_SOURCE_REDIS_MAP);
        Object value = propertyMap.get(CONFIG_KEY_PREFIX + key);
        if (Objects.nonNull(value)) {
            return value;
        }
        return null;
    }

    @Override
    public Object getPropertyFromDB(String key) {
        RMap<String, Object> propertyMap = redissonClient.getMap(PROPERTY_SOURCE_REDIS_MAP);
        PropertySource config = laundryPropertySourceRepository.findByKeyAndStatus(key, true)
                .orElse(null);
        if (Objects.nonNull(config)) {
            propertyMap.put(CONFIG_KEY_PREFIX + key, config.getValue());
            return config.getValue();
        }
        return null;
    }

    @Override
    public void syncRedisProperty() {
        RMap<String, Object> propertyMap = redissonClient.getMap(PROPERTY_SOURCE_REDIS_MAP);
        propertyMap.clear();
        List<PropertySource> properties = laundryPropertySourceRepository.findAll();
        properties.forEach(laundryPropertySource ->
                propertyMap.put(CONFIG_KEY_PREFIX + laundryPropertySource.getKey(), laundryPropertySource.getValue())
        );
    }
}
