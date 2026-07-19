package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.SalesResponseJson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public final class SalesJsonReader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
    }

    private SalesJsonReader() {
    }

    public static Optional<SalesResponseJson> readSales(String resourceName) {
        try (InputStream is = SalesJsonReader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                System.err.println(resourceName + " not found on classpath");
                return Optional.empty();
            }
            return Optional.of(MAPPER.readValue(is, SalesResponseJson.class));
        } catch (IOException e) {
            System.err.println("Failed to read resource '" + resourceName + "': " + e.getMessage());
            return Optional.empty();
        }
    }
}
