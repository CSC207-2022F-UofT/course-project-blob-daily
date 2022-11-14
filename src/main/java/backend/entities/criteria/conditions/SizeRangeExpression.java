package backend.entities.criteria.conditions;

public class SizeRangeExpression extends CriteriaExpression{

    private final int min;
    private final int max;


    public SizeRangeExpression(int min, int max, String target) {
        super(String.valueOf(min) + " - " + String.valueOf(max), "size of range", target);

        this.min = min;
        this.max = max;
    }

    public boolean evaluate() {
        return this.min <= super.getTarget().length() && super.getTarget().length() <= this.max;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }
}
