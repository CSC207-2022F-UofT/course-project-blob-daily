package backend.entities.users.info;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import backend.entities.criteria.conditions.SizeRangeExpression;
import backend.entities.criteria.generatable;

import java.util.ArrayList;
import java.util.List;

public class Password implements generatable {
    // Instance Variables
    private static final Criteria criteria = new Criteria(new ArrayList<>(List.of(
            new SizeRangeExpression(5, 20, null),
            new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
            new ContainsAtleastTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null)
    )));
    private String password;

    // Constructor
    public Password(String password) {
        this.password = password;

        if (password != null) {
            this.isValid(password, criteria);
        }
    }

    @Override
    public String toString() {
        return this.password;
    }

    public static Criteria getCriteria() {
        return criteria;
    }

    public void generatePassword() {
        this.password = this.generate(criteria);
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public String suggestPassword() {
        return this.generate(criteria);
    }

    public boolean isValid() {
        return this.isValid(this.password, criteria);
    }
}
