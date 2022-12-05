package entities.criteria.conditions;

import com.backend.entities.criteria.conditions.SizeRangeExpression;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SizeRangeExpressionTest {
    private SizeRangeExpression sizeRangeExpression;

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
        String expectedTarget = "Testing";
        int expectedMin = 4;
        int expectedMax = 10;

        // Action
        sizeRangeExpression = new SizeRangeExpression(expectedMin, expectedMax, expectedTarget);

        // Assert messages
        String getConstructorMessage = "Unable to construct a criteria object";

        // Asserts
        Assertions.assertEquals(new ArrayList<>(), sizeRangeExpression.getTypeList(), getConstructorMessage);
        Assertions.assertEquals(expectedTarget, sizeRangeExpression.getTarget(), getConstructorMessage);
        Assertions.assertEquals(expectedMin, sizeRangeExpression.getMin(), getConstructorMessage);
        Assertions.assertEquals(expectedMax, sizeRangeExpression.getMax(), getConstructorMessage);
        Assertions.assertEquals("size of range", sizeRangeExpression.getCondition(), getConstructorMessage);
    }

    @Test
    public void evaluateTest() {
        // Expected values
        String expectedTarget = "Testing";
        int expectedMin = 4;
        int expectedMax = 10;

        // Action
        sizeRangeExpression = new SizeRangeExpression(expectedMin, expectedMax, expectedTarget);

        // Assert messages
        String evaluateMessage = "Unable to evaluate a valid target accurately";

        // Asserts
        Assertions.assertTrue(sizeRangeExpression.evaluate(), evaluateMessage);
    }

    @Test
    public void evaluateInvalidTest() {
        // Expected values
        String expectedTarget = "test";
        int expectedMin = 9;
        int expectedMax = 10;

        // Action
        sizeRangeExpression = new SizeRangeExpression(expectedMin, expectedMax, expectedTarget);

        // Assert messages
        String evaluateMessage = "Unable to evaluate an invalid target accurately";

        // Asserts
        Assertions.assertFalse(sizeRangeExpression.evaluate(), evaluateMessage);
    }
}
