package com.stanzaliving.secure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanzaliving.secure.dto.common.ResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Log4j2
@Service
public class MapperUtils1 {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T mapStringToDto(String string, Class<T> tClass) {
        T result = null;

        try {
            result = objectMapper.readValue(string, tClass);
        } catch (Exception e) {
            log.info("Exception caught while mapping string to dto : {} where DTO Class is : {} ", e, tClass.getName());
        }

        return result;
    }

    public String writeValueAsString(Object object) {
        String requestBody = null;

        try {
            requestBody = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Exception caught while mapping to string : {} from Object : {}", e, object);
        }

        return requestBody;
    }

    public <T> T mapObjectToClass(Object object, Class<T> tClass) {
        String stringResult = writeValueAsString(object);
        T result = mapStringToDto(stringResult, tClass);
        return result;
    }

    public <T> T mapObjectToClass(Object object, TypeReference<T> typeReference) {
        T result = null;
        try {
            result = objectMapper.convertValue(object, typeReference);
        } catch (IllegalArgumentException ex) {
            log.warn("Exception occurred while converting object {} to type {}", object, typeReference);
        }
        return result;
    }

    public List<?> mapResponseEntityToList(ResponseDto<?> responseDto) {
        List<?> results = null;

        try {
            results = objectMapper.convertValue(responseDto.getData(), new TypeReference<List<?>>() {
            });
        } catch (IllegalArgumentException e) {
            log.error("Exception caught while responseDto: {} to list of class: {}", e, responseDto);
        }

        return results;
    }

}
