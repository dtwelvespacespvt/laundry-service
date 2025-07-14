package com.stanzaliving.secure.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.time.LocalTime;

/**
 *
 * @author naveen
 *
 * @date 27-Dec-2019
 */
public class Java8LocalTimeDeserializer extends StdDeserializer<LocalTime> {

    private static final long serialVersionUID = 1L;

    public Java8LocalTimeDeserializer() {
        super(LocalTime.class);
    }

    @Override
    public LocalTime deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {

        LocalTime localDate = null;

        if (jsonparser.isExpectedStartObjectToken()) {

            JsonNode node = jsonparser.getCodec().readTree(jsonparser);
            ObjectNode obj = (ObjectNode) node;
            int hour = obj.get("hour").asInt();
            int minute = obj.get("minute").asInt();
            int second = obj.get("second").asInt();
            int nano = obj.get("nano").asInt();

            localDate = LocalTime.of(hour, minute, second, nano);

        } else {
            localDate = LocalTime.parse(jsonparser.getText());
        }
        return localDate;
    }
}