package entities.criteria;

import com.backend.entities.criteria.Criteria;
import com.backend.entities.criteria.Generatable;
import com.backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import com.backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import com.backend.entities.criteria.conditions.SizeExpression;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GeneratableTest implements Generatable {

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void isValidTest() {
        // Expected values
        Criteria criteria = new Criteria(new ArrayList<>(List.of(
                new SizeExpression(3, null),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special")), null)
        )));

        // Action
        boolean result = this.isValid("*1a", criteria);

        // Assert messages
        String isValidMessage = "Unexpectedly found a valid target to be invalid";

        // Asserts
        Assertions.assertTrue(result, isValidMessage);
    }

    @Test
    public void isValidInvalidTest() {
        // Expected values
        Criteria criteria = new Criteria(new ArrayList<>(List.of(
                new SizeExpression(3, null),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special")), null)
        )));

        // Action
        boolean result = this.isValid("*1aa", criteria);

        // Assert messages
        String isValidMessage = "Unexpectedly found a valid target to be invalid";

        // Asserts
        Assertions.assertFalse(result, isValidMessage);
    }

    @Test
    public void generateTest() {
        // Expected values
        Criteria criteria = new Criteria(new ArrayList<>(List.of(
                new SizeExpression(3, null),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special")), null)
        )));

        // Action
        boolean result = this.isValid(this.generate(criteria), criteria);

        // Assert messages
        String generateMessage = "Unexpectedly unable to generate based on a valid criteria";

        // Asserts
        Assertions.assertTrue(result, generateMessage);
    }
}
