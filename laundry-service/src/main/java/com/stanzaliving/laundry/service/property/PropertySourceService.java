package com.stanzaliving.laundry.service.property;

public interface PropertySourceService {

    Object getPropertyFromRedis(String key);

    Object getPropertyFromDB(String key);

    void syncRedisProperty();
}
