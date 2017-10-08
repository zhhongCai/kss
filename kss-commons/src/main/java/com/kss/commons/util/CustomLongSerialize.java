package com.kss.commons.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class CustomLongSerialize extends JsonSerializer<Object> {
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        if (null != value) {
            jgen.writeString(value.toString());
        } else {
            jgen.writeObject(value);
        }
    }
}
