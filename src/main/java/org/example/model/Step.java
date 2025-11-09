package org.example.model;

import java.util.List;
import java.util.Map;

public class Step {
    public String id;
    public String type;
    public HttpRequest request;
    public Map<String, String> extract;
    public List<Assertion> assertions;

    public static class HttpRequest {
        public String method;
        public String url;
        public Map<String, String> headers;
        public Object body;
    }
}