package com.backend.entities.criteria;

import com.backend.entities.criteria.conditions.CriteriaExpression;
import com.backend.entities.criteria.conditions.IConditionHandler;
import com.backend.entities.criteria.conditions.SizeExpression;
import com.backend.entities.criteria.conditions.SizeRangeExpression;
import com.backend.error.exceptions.CriteriaException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Interface to support generating a string for a given criteria
 */
public interface Generatable extends Verifiable, IConditionHandler {
    /**
     * Generate a new string given a criteria
     * @param criteria of type Criteria, criteria to generate a new valid string with
     * @return A new string based on the given criteria
     */
    default String generate(Criteria criteria) {
        // Get min size and types based on criteria
        int sizeParam = 0;
        ArrayList<String> types = new ArrayList<>();

        for (CriteriaExpression expression : criteria.getExpressions()) {
            if(expression instanceof SizeExpression) {
                sizeParam = Integer.parseInt(expression.getValue());
            } else if (expression instanceof SizeRangeExpression) {
                sizeParam = ((SizeRangeExpression) expression).getMax();
            } else {
                types.addAll(expression.getTypeList());
            }
        }

        // Remove duplicate types
        types = (ArrayList<String>) types.stream().distinct().collect(Collectors.toList());

        // Generate the new string based on the previous info
        StringBuilder newString = new StringBuilder();
        Random random = new Random();
        HashMap<String, String> legend = criteria.getLegend();

        int typeIndex = 0;

        while(newString.length() < sizeParam) {
            String type = legend.get(types.get(typeIndex));
            int index = random.nextInt(type.length());
            newString.append(type.charAt(index));

            if(types.size() == (typeIndex + 1)) {
                typeIndex = 0;
            } else {
                typeIndex++;
            }
        }

        if (!this.isValid(newString.toString(), criteria)) {
            String errorMessage = String.format("Invalid criteria, %s%n", criteria);
            this.logError(new CriteriaException(errorMessage));
        }

        return newString.toString();
    }
}
