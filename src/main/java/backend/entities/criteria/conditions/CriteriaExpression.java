package backend.entities.criteria.conditions;

import backend.error.exceptions.ConditionException;
import backend.error.handlers.LogHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        
        this.checkTypes(typeList);
    }
    
    // Validate Expression inputs
    public boolean checkTypes(List<String> typeList) {
        for (String type : typeList) {
            if (!legend.containsKey(type)) {
                LogHandler.logError(new ConditionException(String.format("The type: %s is not a valid type%n", type)));
                return false;
            }
        }
        return true;
    }

    // Log a condition error
    protected void logError(String message) {
        LogHandler.logError(new ConditionException(message));
    }

    // Getters
    public String getExpression() {
        if (this.typeList.size() > 0) {
            return String.format("contains types %s with the target of %s", this.typeList.toString(), this.target);
        }
        return String.format("%s : %s with the target of %s", this.condition, this.value, this.target);
    }

    public List<String> getTypeList() {
        return this.typeList;
    }

    public String getCondition() {
        return this.condition;
    }

    public String getTarget() {
        return this.target;
    }

    public String getValue() {
        return this.value;
    }

    public HashMap<String, String> getLegend() {
        return legend;
    }

    // Setter
    public void setTarget(String newTarget) {
        this.target = newTarget;
    }
    
    // Evaluation of expression
    public abstract boolean evaluate();
}
