package org.example.executor;

import java.util.HashMap;
import java.util.Map;

public class ExecutionContext {
    private final Map<String, Object> variables = new HashMap<>();

    public void put(String key, Object value) {
        variables.put(key, value);
    }

    public Object get(String key) {
        return variables.get(key);
    }

    public String resolveString(String input) {
        if (input == null) return null;
        String result = input;
        int start;
        while ((start = result.indexOf("${")) >= 0) {
            int end = result.indexOf("}", start);
            if (end < 0) break;
            String name = result.substring(start + 2, end);
            Object value = variables.getOrDefault(name, "");
            result = result.substring(0, start) + value + result.substring(end + 1);
        }
        return result;
    }

    @Override
    public String toString() {
        return "ExecutionContext{" +
                "variables=" + variables +
                '}';
    }
}