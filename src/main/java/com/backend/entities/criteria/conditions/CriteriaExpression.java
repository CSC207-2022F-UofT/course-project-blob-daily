package com.backend.entities.criteria.conditions;

import com.backend.error.exceptions.ConditionException;
import com.backend.error.handlers.LogHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Abstract class (Builder Design Pattern) to support the general structure for a Criteria Expression
 */
public abstract class CriteriaExpression {
    // Instance Variables
    private final String value;
    private final List<String> typeList;
    private final String condition;
    private String target;

    private static final HashMap<String, String> legend = new HashMap<>();

    static{
        legend.put("special", ",./;'[]=-`~!@#$%^&*()_+}{\":?><|");
        legend.put("number", "0123456789");
        legend.put("uppercase", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        legend.put("lowercase", legend.get("uppercase").toLowerCase());
        legend.put("letter", legend.get("uppercase") + legend.get("lowercase"));
    }

    // Constructors
    public CriteriaExpression(String value, String condition, String target) {
        this.value = value;
        this.target = target;
        this.condition = condition;
        this.typeList = new ArrayList<>();
    }

    public CriteriaExpression(List<String> typeList, String target) {
        this.value = null;
        this.condition = null;
        this.target = target;
        this.typeList = typeList;
        
        if (!this.checkTypes(typeList)) LogHandler.logWarning("Illegal type may cause an unexpected error!");
    }

    /**
     * Check if the given types (from typeList) only contains valid types
     * @param typeList of type ArrayList<String>, typeList to be validated
     * @return Whether typeList only contains valid types
     */
    public boolean checkTypes(List<String> typeList) {
        for (String type : typeList) {
            if (!legend.containsKey(type)) {
                LogHandler.logError(new ConditionException(String.format("The type: %s is not a valid type%n", type)));
                return false;
            }
        }
        return true;
    }

    /**
     * Automatically log a message as a Condition Exception
     * @param message of type String, message to be logged
     */
    protected void logError(String message) {
        LogHandler.logError(new ConditionException(message));
    }

    // Getters

    /**
     * Get the current expression for this Criteria Expression instance
     * @return the string representation of the expression (with target reference)
     */
    public String getExpression() {
        if (this.typeList.size() > 0) {
            return String.format("contains types %s with the target of %s", this.typeList, this.target);
        }
        return String.format("%s : %s with the target of %s", this.condition, this.value, this.target);
    }

    /**
     * Get the current list of types for this Criteria Expression
     * @return the typeList variable
     */
    public List<String> getTypeList() {
        return this.typeList;
    }

    /**
     * Get the current string representation of criteria for this Criteria Expression
     * @return the criteria variable
     */
    @SuppressWarnings("unused")
    public String getCondition() {
        return this.condition;
    }

    /**
     * Get the current string representation of target for this Criteria Expression
     * @return the target variable
     */
    public String getTarget() {
        return this.target;
    }

    /**
     * Get the current string representation of value for this Criteria Expression
     * @return the value variable
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Get the current legend of types for this Criteria Expression
     * @return the legend variable
     */
    public HashMap<String, String> getLegend() {
        return legend;
    }

    // Setter

    /**
     * Set the current target to the given newTarget
     * @param newTarget of type String, newTarget to set the current target to
     */
    public void setTarget(String newTarget) {
        this.target = newTarget;
    }

    /**
     * Abstract method to ensure all Criteria Expressions have a unique implementation for evaluation of their target given value/typeList
     * @return Whether the current target is satisfied by the known conditional parameters (value/typeList)
     */
    public abstract boolean evaluate();
}
