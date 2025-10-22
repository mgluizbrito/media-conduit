package io.github.mgluizbrito.mediaconduit_api.cli;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class YtDlpDateDeserializer extends StdDeserializer<LocalDate> {

    public YtDlpDateDeserializer() {
        this(null);
    }

    public YtDlpDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        String dateString = jp.getText();

        if (dateString == null || dateString.isEmpty()) return null;

        return LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE);
    }
}
