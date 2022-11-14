package backend.entities.criteria.conditions;

public class SizeExpression extends CriteriaExpression{

    private final int value;


    public SizeExpression(int value, String target) {
        super(String.valueOf(value), "size", target);

        this.value = value;
    }

    public boolean evaluate() {
        return super.getTarget().length() == this.value;
    }
}
