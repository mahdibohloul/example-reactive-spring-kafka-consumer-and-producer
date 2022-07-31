package com.example.reactivekafkaconsumerandproducer.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

@Component
public class JacksonDeserializer extends JsonDeserializer {
    public JacksonDeserializer() {
        super(createObjectMapper());
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        return super.deserialize(topic, data);
    }

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data) {
        return super.deserialize(topic, headers, data);
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
//                        .activateDefaultTyping(
//                        BasicPolymorphicTypeValidator.builder()
//                                .allowIfBaseType(Object.class)
//                                .build(),
//                        ObjectMapper.DefaultTyping.EVERYTHING
//                )
    }
}
