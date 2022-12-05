package entities.criteria.conditions;

import com.backend.entities.criteria.conditions.SizeExpression;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SizeExpressionTest {
    private SizeExpression sizeExpression;

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
        String expectedTarget = "Test";
        int expectedValue = 4;

        // Action
        sizeExpression = new SizeExpression(expectedValue, expectedTarget);

        // Assert messages
        String getConstructorMessage = "Unable to construct a criteria object";

        // Asserts
        Assertions.assertEquals(new ArrayList<>(), sizeExpression.getTypeList(), getConstructorMessage);
        Assertions.assertEquals(expectedTarget, sizeExpression.getTarget(), getConstructorMessage);
        Assertions.assertEquals(String.valueOf(expectedValue), sizeExpression.getValue(), getConstructorMessage);
        Assertions.assertEquals("size", sizeExpression.getCondition(), getConstructorMessage);
    }

    @Test
    public void evaluateTest() {
        // Expected values
        String expectedTarget = "Test";
        int expectedValue = 4;

        // Action
        sizeExpression = new SizeExpression(expectedValue, expectedTarget);

        // Assert messages
        String evaluateMessage = "Unable to evaluate a valid target accurately";

        // Asserts
        Assertions.assertTrue(sizeExpression.evaluate(), evaluateMessage);
    }

    @Test
    public void evaluateInvalidTest() {
        // Expected values
        String expectedTarget = "Tst";
        int expectedValue = 4;

        // Action
        sizeExpression = new SizeExpression(expectedValue, expectedTarget);

        // Assert messages
        String evaluateMessage = "Unable to evaluate an invalid target accurately";

        // Asserts
        Assertions.assertFalse(sizeExpression.evaluate(), evaluateMessage);
    }
}
