package org.example.http;

public class HttpResponseWrapper {
    public final int status;
    public final String body;

    public HttpResponseWrapper(int status, String body) {
        this.status = status;
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponseWrapper{" +
                "status=" + status +
                ", body='" + (body != null ? body.substring(0, Math.min(body.length(), 200)) : "") + '\'' +
                '}';
    }
}