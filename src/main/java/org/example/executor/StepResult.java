package org.example.executor;

public class StepResult {
    public final String stepId;
    public final boolean success;
    public final String message;

    public StepResult(String stepId, boolean success, String message) {
        this.stepId = stepId;
        this.success = success;
        this.message = message;
    }

    @Override
    public String toString() {
        return "StepResult{" +
                "stepId='" + stepId + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}