package org.example;

import org.example.executor.ScenarioExecutor;
import org.example.executor.StepResult;
import org.example.io.ScenarioLoader;
import org.example.model.Scenario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScenarioExecutorTest {

    @Test
    void runExampleScenario() throws Exception {
        ScenarioLoader loader = new ScenarioLoader();
        Scenario scenario = loader.loadFromResource("/scenarios/example-purchase.yaml");

        ScenarioExecutor executor = new ScenarioExecutor();
        List<StepResult> results = executor.run(scenario);

        // Проверяем, что все шаги прошли успешно
        assertTrue(results.stream().allMatch(r -> r.success),
                "Не все шаги сценария выполнились успешно");

        // Проверим, что сценарий не пустой
        assertFalse(results.isEmpty(), "Сценарий не должен быть пустым");
    }
}
