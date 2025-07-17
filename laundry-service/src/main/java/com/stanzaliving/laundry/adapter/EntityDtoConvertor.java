package com.stanzaliving.laundry.adapter;

@FunctionalInterface
public interface EntityDtoConvertor<R,T> {

    R convert(T t, Class<R> clazz);
}
