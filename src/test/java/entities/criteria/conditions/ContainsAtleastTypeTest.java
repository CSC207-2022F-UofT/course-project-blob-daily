package entities.criteria.conditions;

import com.backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ContainsAtleastTypeTest {
    private ContainsAtleastTypeExpression containsAtleastTypeExpression;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testConstructor() {
        // Expected values
        ArrayList<String> expectedTypeList = new ArrayList<>(List.of("letter", "number"));
        String expectedTarget = "abc123";

        // Action
        containsAtleastTypeExpression = new ContainsAtleastTypeExpression(expectedTypeList, expectedTarget);

        // Assert messages
        String getConstructorMessage = "Unable to construct a criteria object";

        // Asserts
        Assertions.assertEquals(expectedTypeList, containsAtleastTypeExpression.getTypeList(), getConstructorMessage);
        Assertions.assertEquals(expectedTarget, containsAtleastTypeExpression.getTarget(), getConstructorMessage);
        Assertions.assertNull(containsAtleastTypeExpression.getValue(), getConstructorMessage);
        Assertions.assertNull(containsAtleastTypeExpression.getCondition(), getConstructorMessage);
    }

    @Test
    public void evaluateTest() {
        // Expected values
        ArrayList<String> expectedTypeList = new ArrayList<>(List.of("letter", "number"));
        String expectedTarget = "abc123";

        // Action
        containsAtleastTypeExpression = new ContainsAtleastTypeExpression(expectedTypeList, expectedTarget);

        // Assert messages
        String evaluateMessage = "Unable to evaluate a valid target accurately";

        // Asserts
        Assertions.assertTrue(containsAtleastTypeExpression.evaluate(), evaluateMessage);
    }

    @Test
    public void evaluateInvalidTest() {
        // Expected values
        ArrayList<String> expectedTypeList = new ArrayList<>(List.of("letter", "number"));
        String expectedTarget = "abc";

        // Action
        containsAtleastTypeExpression = new ContainsAtleastTypeExpression(expectedTypeList, expectedTarget);

        // Assert messages
        String evaluateMessage = "Unable to evaluate an invalid target accurately";

        // Asserts
        Assertions.assertFalse(containsAtleastTypeExpression.evaluate(), evaluateMessage);
    }
}
