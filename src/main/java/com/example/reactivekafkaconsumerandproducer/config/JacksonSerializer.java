package com.example.reactivekafkaconsumerandproducer.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

@Component
public class JacksonSerializer extends JsonSerializer {

    public JacksonSerializer() {
        super(createObjectMapper());
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
//                .activateDefaultTyping(
//                        BasicPolymorphicTypeValidator.builder()
//                                .allowIfBaseType(Object.class)
//                                .build(),
//                        ObjectMapper.DefaultTyping.EVERYTHING
//                );
    }
}
