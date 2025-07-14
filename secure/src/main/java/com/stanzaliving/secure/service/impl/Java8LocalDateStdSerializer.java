package com.stanzaliving.secure.service.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;

/**
 *
 * @author naveen
 *
 * @date 29-Sep-2019
 */
public class Java8LocalDateStdSerializer extends StdSerializer<LocalDate> {

    private static final long serialVersionUID = 1L;

    public Java8LocalDateStdSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(value.toString());
    }

}