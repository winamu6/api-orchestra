package org.example.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.http.HttpClientWrapper;
import org.example.http.HttpResponseWrapper;
import org.example.model.Assertion;
import org.example.model.Scenario;
import org.example.model.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ScenarioExecutor {
    private static final Logger log = LoggerFactory.getLogger(ScenarioExecutor.class);
    private final HttpClientWrapper httpClient = new HttpClientWrapper();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<StepResult> run(Scenario scenario) throws Exception {
        log.info("Запуск сценария: {}", scenario.id);
        List<StepResult> results = new ArrayList<>();
        ExecutionContext context = new ExecutionContext();

        if (scenario.variables != null) {
            scenario.variables.forEach(context::put);
        }

        for (Step step : scenario.steps) {
            try {
                log.info("→ Шаг: {}", step.id);

                if (!"http".equalsIgnoreCase(step.type)) {
                    results.add(new StepResult(step.id, false, "Неизвестный тип шага: " + step.type));
                    continue;
                }

                String method = step.request.method != null ? step.request.method.toUpperCase() : "GET";
                String url = context.resolveString(step.request.url);
                String body = step.request.body != null
                        ? context.resolveString(mapper.writeValueAsString(step.request.body))
                        : null;

                HttpResponseWrapper response = httpClient.execute(method, url, step.request.headers, body);

                if (step.assertions != null) {
                    for (Assertion assertion : step.assertions) {
                        if (assertion.status != null && assertion.status != response.status) {
                            throw new AssertionError("Ожидался статус " + assertion.status + ", а пришёл " + response.status);
                        }

                        if (assertion.jsonPath != null && assertion.equals != null) {
                            JsonNode json = mapper.readTree(response.body);
                            JsonNode node = json.at(assertion.jsonPath); // JSON Pointer ("/field")
                            String actual = node.isMissingNode() ? null : node.asText();

                            if (actual == null || !actual.equals(assertion.equals)) {
                                throw new AssertionError("Проверка не прошла для " + assertion.jsonPath +
                                        ": ожидалось '" + assertion.equals + "', а пришло '" + actual + "'");
                            }
                        }
                    }
                }

                if (step.extract != null) {
                    JsonNode json = mapper.readTree(response.body);
                    for (var e : step.extract.entrySet()) {
                        JsonNode node = json.at(e.getValue());
                        if (!node.isMissingNode()) {
                            context.put(e.getKey(), node.asText());
                            log.info("Извлечено: {} = {}", e.getKey(), node.asText());
                        }
                    }
                }

                results.add(new StepResult(step.id, true, "OK"));
            } catch (Exception e) {
                log.error("Ошибка на шаге {}: {}", step.id, e.getMessage());
                results.add(new StepResult(step.id, false, e.getMessage()));
                break;
            }
        }

        log.info("Сценарий завершён. Результаты:");
        results.forEach(r -> log.info("  {} → {}", r.stepId, r.success ? "✅" : "❌"));
        return results;
    }
}