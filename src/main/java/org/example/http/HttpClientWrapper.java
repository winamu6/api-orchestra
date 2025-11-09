package org.example.http;

import okhttp3.*;
import java.time.Duration;
import java.util.Map;

public class HttpClientWrapper {

    private final OkHttpClient client;

    public HttpClientWrapper() {
        this.client = new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(30))
                .build();
    }

    public HttpResponseWrapper execute(String method, String url, Map<String, String> headers, String body) throws Exception {
        RequestBody requestBody = null;

        if (body != null && !body.isEmpty()) {
            requestBody = RequestBody.create(body, MediaType.get("application/json; charset=utf-8"));
        } else if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
            requestBody = RequestBody.create("{}", MediaType.get("application/json; charset=utf-8"));
        }

        Request.Builder builder = new Request.Builder().url(url);

        if (headers != null) {
            headers.forEach(builder::addHeader);
        }

        builder.method(method.toUpperCase(), requestBody);

        try (Response response = client.newCall(builder.build()).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            return new HttpResponseWrapper(response.code(), responseBody);
        }
    }
}
