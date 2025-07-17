package com.stanzaliving.laundry.adapter;

import com.stanzaliving.laundry.dto.entityDto.AbstractEntityDto;
import com.stanzaliving.laundry.entity.AbstractJpaEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Log4j2
@Component
public class JpaEntityToDtoConvertor<R extends AbstractEntityDto, T extends AbstractJpaEntity>
        implements EntityDtoConvertor<R, T> {

    @Override
    public R convert(@NotNull T abstractJpaEntity, Class<R> clazz) {
        R dto = createInstance(clazz);
        BeanUtils.copyProperties(abstractJpaEntity, dto);
        return dto;
    }

    private <X> X createInstance(Class<X> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Could not instantiate " + clazz.getName(), e);
            throw new RuntimeException(e);
        }
    }
}
