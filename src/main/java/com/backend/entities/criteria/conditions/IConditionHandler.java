package com.backend.entities.criteria.conditions;

public interface IConditionHandler {
    default void logError(Exception e) {
    }

    default void logWarning(String message) {
    }
}
