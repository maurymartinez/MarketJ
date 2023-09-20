package com.market.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> String getJsonValueOf(T value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    public static <T> T getValueOfJson(String json, Class<T> clas) throws JsonProcessingException {
        return objectMapper.readValue(json, clas);
    }
}
