package backend.entites.criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CriteriaExpression {
    // Instance Variables
    private final String value;
    private final List<String> conditionList;
    private final String condition;
    private final String target;

    private final HashMap<String, String> legend = new HashMap<>();

    // Constructors
    public CriteriaExpression(String value, String condition, String target) {
        this.value = value;
        this.target = target;
        this.condition = condition;
        this.conditionList = new ArrayList<>();

        this.legend.put("special", ",./;'[]=-`~!@#$%^&*()_+}{\":?><|");
        this.legend.put("number", "0123456789");
        this.legend.put("uppercase", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        this.legend.put("lowercase", this.legend.get("uppercase").toLowerCase());
        this.legend.put("letter", this.legend.get("uppercase") + this.legend.get("lowercase"));
    }

    public CriteriaExpression(List<String> conditionList, String target) {
        this.value = null;
        this.condition = null;
        this.target = target;
        this.conditionList = conditionList;

        this.legend.put("special", ",./;'[]=-`~!@#$%^&*()_+}{\":?><|");
        this.legend.put("number", "0123456789");
        this.legend.put("uppercase", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        this.legend.put("lowercase", this.legend.get("uppercase").toLowerCase());
        this.legend.put("letter", this.legend.get("uppercase") + this.legend.get("lowercase"));
    }

    public boolean evaluate(String overriddenCondition, String overriddenValue) {
        String testedCondition;
        String testedValue;

        if (!(overriddenCondition == null || overriddenCondition.isEmpty())) testedCondition = overriddenCondition; else testedCondition = this.condition;
        if (!(overriddenValue == null || overriddenValue.isEmpty())) testedValue = overriddenValue; else testedValue = this.value;

        if (this.conditionList.size() > 0 && testedCondition == null) {
            StringBuilder typeElements = new StringBuilder();
            for (String condition : this.conditionList) {
                if(!this.legend.containsKey(condition)) {
                    System.out.printf("The condition %s is not a valid condition type.%n", condition);
                    return false;
                }
                typeElements.append(this.legend.get(condition));
            }
            return this.evaluate("contains only", typeElements.toString());
        }

        switch (testedCondition) {
            case "contains only":
                for(char c : this.target.toCharArray()) {
                    if (testedValue.indexOf(c) == -1) return false;
                }

                return true;
            case "contains value":
                return this.target.contains(testedValue);
            case "contains type":
                String type;

                if(!this.legend.containsKey(testedValue)) {
                    System.out.printf("The condition %s is not a valid condition type.%n", testedCondition);
                    return false;
                }

                type = this.legend.get(testedValue);

                 for (char c : this.target.toCharArray()) {
                    if (type.indexOf(c) != -1) return true;
                 }
                return false;
            case "at least size":
                return this.target.length() >= Integer.parseInt(testedValue);
            case "at most size":
                return this.target.length() <= Integer.parseInt(testedValue);
            case "size":
                return this.target.length() == Integer.parseInt(testedValue);
            default:
                System.out.printf("The condition %s is not a valid condition type.%n", testedCondition);
                return false;
        }
    }

    public String getExpression() {
        if (this.condition == null) {
            return String.format("contains types %s with the target of %s", this.conditionList.toString(), this.target);
        }
        return String.format("%s, %s with the target of %s", this.condition, this.value, this.target);
    }
}
