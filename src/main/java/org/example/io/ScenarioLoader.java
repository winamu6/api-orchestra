package org.example.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.example.model.Scenario;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ScenarioLoader {
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public Scenario loadFromFile(String path) throws IOException {
        return mapper.readValue(new File(path), Scenario.class);
    }

    public Scenario loadFromResource(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);
            return mapper.readValue(is, Scenario.class);
        }
    }

    public Scenario loadFromString(String yamlContent) throws IOException {
        return mapper.readValue(yamlContent, Scenario.class);
    }
}
