package com.stanzaliving.secure.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author naveen
 *
 * @date 29-Sep-2019
 */
public class Java8LocalDateStdDeserializer extends StdDeserializer<LocalDate> {

    private static final long serialVersionUID = 1L;

    public Java8LocalDateStdDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {

        LocalDate localDate = null;

        if (jsonparser.isExpectedStartObjectToken()) {

            JsonNode node = jsonparser.getCodec().readTree(jsonparser);
            ObjectNode obj = (ObjectNode) node;

            int year = obj.get("year").asInt();
            int month = obj.get("monthValue").asInt();
            int day = obj.get("dayOfMonth").asInt();

            localDate = LocalDate.of(year, month, day);

        } else if (jsonparser.isExpectedStartArrayToken()) {

            JsonNode node = jsonparser.getCodec().readTree(jsonparser);
            if (node.isArray()) {

                ArrayNode obj = (ArrayNode) node;

                int year = obj.get(0).asInt();
                int month = obj.get(1).asInt();
                int day = obj.get(2).asInt();

                localDate = LocalDate.of(year, month, day);

            }

        } else {

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

            try {
                localDate = LocalDate.parse(jsonparser.getText(), dateTimeFormatter);
            } catch (Exception e) {
                localDate = LocalDate.parse(jsonparser.getText());
            }


        }
        return localDate;
    }

}