package com.questpets.entities.users.info;

import com.questpets.entities.criteria.Criteria;
import com.questpets.entities.criteria.conditions.ContainsAtleastTypeExpression;
import com.questpets.entities.criteria.conditions.ContainsOnlyTypeExpression;
import com.questpets.entities.criteria.conditions.SizeRangeExpression;
import com.questpets.entities.criteria.generatable;

import java.util.ArrayList;
import java.util.List;

public class Username implements generatable {
    // Instance Variables
    private String username;

    private static Criteria criteria = new Criteria(new ArrayList<>(List.of(
            new SizeRangeExpression(5, 20, null),
            new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter")), null),
            new ContainsAtleastTypeExpression(new ArrayList<>(List.of("number", "letter", "uppercase", "lowercase")), null)
    )));

    // Constructor
    public Username(String username) {
        this.username = username;

        if (username != null) {
            this.isValid(username, criteria);
        }
    }

    @Override
    public String toString() {
        return this.username;
    }

    public void generateUsername() {
        this.username = this.generate(criteria);
    }

    public String suggestUsername() {
        return this.generate(criteria);
    }

    public boolean isValid() {
        return this.isValid(this.username, criteria);
    }
}
