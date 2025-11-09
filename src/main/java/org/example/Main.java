package org.example;

import org.example.executor.ScenarioExecutor;
import org.example.io.ScenarioLoader;
import org.example.model.Scenario;

import java.util.List;
import org.example.executor.StepResult;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== API ORCHESTRA ===");

            // 1. Загружаем сценарий из ресурсов
            ScenarioLoader loader = new ScenarioLoader();
            Scenario scenario = loader.loadFromResource("/scenarios/example-purchase.yaml");

            // 2. Выполняем сценарий
            ScenarioExecutor executor = new ScenarioExecutor();
            List<StepResult> results = executor.run(scenario);

            // 3. Выводим итоги
            System.out.println("\n=== Результаты выполнения ===");
            for (StepResult result : results) {
                System.out.printf("→ %s: %s (%s)%n",
                        result.stepId,
                        result.success ? "✅ Успех" : "❌ Ошибка",
                        result.message);
            }

            System.out.println("\n=== Завершено ===");

        } catch (Exception e) {
            System.err.println("Ошибка выполнения сценария: " + e.getMessage());
            e.printStackTrace();
        }
    }
}