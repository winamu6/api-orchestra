package org.example.model;

import java.util.List;
import java.util.Map;

public class Scenario {
    public String id;
    public String description;
    public Map<String, Object> variables;
    public List<Step> steps;
}