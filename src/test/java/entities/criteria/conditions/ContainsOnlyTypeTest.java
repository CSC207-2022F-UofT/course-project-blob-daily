package entities.criteria.conditions;

import com.backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ContainsOnlyTypeTest {
    private ContainsOnlyTypeExpression containsOnlyTypeExpression;

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
        containsOnlyTypeExpression = new ContainsOnlyTypeExpression(expectedTypeList, expectedTarget);

        // Assert messages
        String getConstructorMessage = "Unable to construct a criteria object";

        // Asserts
        Assertions.assertEquals(expectedTypeList, containsOnlyTypeExpression.getTypeList(), getConstructorMessage);
        Assertions.assertEquals(expectedTarget, containsOnlyTypeExpression.getTarget(), getConstructorMessage);
        Assertions.assertNull(containsOnlyTypeExpression.getValue(), getConstructorMessage);
        Assertions.assertNull(containsOnlyTypeExpression.getCondition(), getConstructorMessage);
    }

    @Test
    public void evaluateTest() {
        // Expected values
        ArrayList<String> expectedTypeList = new ArrayList<>(List.of("letter", "number"));
        String expectedTarget = "abc123";

        // Action
        containsOnlyTypeExpression = new ContainsOnlyTypeExpression(expectedTypeList, expectedTarget);

        // Assert messages
        String evaluateMessage = "Unable to evaluate a valid target accurately";

        // Asserts
        Assertions.assertTrue(containsOnlyTypeExpression.evaluate(), evaluateMessage);
    }

    @Test
    public void evaluateInvalidTest() {
        // Expected values
        ArrayList<String> expectedTypeList = new ArrayList<>(List.of("letter", "number"));
        String expectedTarget = "abc12*3";

        // Action
        containsOnlyTypeExpression = new ContainsOnlyTypeExpression(expectedTypeList, expectedTarget);

        // Assert messages
        String evaluateMessage = "Unable to evaluate an invalid target accurately";

        // Asserts
        Assertions.assertFalse(containsOnlyTypeExpression.evaluate(), evaluateMessage);
    }
}
