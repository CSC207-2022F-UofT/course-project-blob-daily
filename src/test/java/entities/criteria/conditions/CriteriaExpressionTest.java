package entities.criteria.conditions;

import com.backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import com.backend.entities.criteria.conditions.CriteriaExpression;
import com.backend.entities.criteria.conditions.SizeExpression;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CriteriaExpressionTest {
    private CriteriaExpression criteriaExpression;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testConstructorValueStringTarget() {
        // Expected values
        String expectedTarget = "Test";
        int expectedValue = 4;

        // Action
        criteriaExpression = new SizeExpression(expectedValue, expectedTarget);

        // Assert messages
        String getConstructorMessage = "Unable to construct a criteria object";

        // Asserts
        Assertions.assertEquals(new ArrayList<>(), criteriaExpression.getTypeList(), getConstructorMessage);
        Assertions.assertEquals(expectedTarget, criteriaExpression.getTarget(), getConstructorMessage);
        Assertions.assertEquals(String.valueOf(expectedValue), criteriaExpression.getValue(), getConstructorMessage);
        Assertions.assertEquals("size", criteriaExpression.getCondition(), getConstructorMessage);
    }

    @Test
    public void testConstructorTypeListTarget() {
        // Expected values
        String expectedTarget = "Test";
        ArrayList<String> expectedTypeList = new ArrayList<>(List.of("letter", "special"));

        // Action
        criteriaExpression = new ContainsOnlyTypeExpression(expectedTypeList, expectedTarget);

        // Assert messages
        String getConstructorMessage = "Unable to construct a criteria object";

        // Asserts
        Assertions.assertEquals(expectedTypeList, criteriaExpression.getTypeList(), getConstructorMessage);
        Assertions.assertEquals(expectedTarget, criteriaExpression.getTarget(), getConstructorMessage);
        Assertions.assertNull(criteriaExpression.getValue(), getConstructorMessage);
        Assertions.assertNull(criteriaExpression.getCondition(), getConstructorMessage);
    }

    @Test
    public void getExpressionTest() {
        // Expected values
        String expectedTarget = "Test";
        int expectedValue = 4;

        // Action
        criteriaExpression = new SizeExpression(expectedValue, expectedTarget);

        // Assert messages
        String getCriteriaExpressionMessage = "Incorrect expression string returned";

        // Asserts
        Assertions.assertEquals("size : 4 with the target of Test", criteriaExpression.getExpression(), getCriteriaExpressionMessage);
    }

    @Test
    public void checkTypeListTest() {
        // Expected values
        ArrayList<String> expectedTypeList = new ArrayList<>(List.of("letter", "special"));

        // Action
        criteriaExpression = new ContainsOnlyTypeExpression(expectedTypeList, "");

        // Assert messages
        String checkTypeListMessage = "Incorrect response given for a valid list of criteria types";

        // Asserts
        Assertions.assertTrue(criteriaExpression.checkTypes(expectedTypeList), checkTypeListMessage);
    }

    @Test
    public void checkTypeListInvalidTest() {
        // Expected values
        ArrayList<String> expectedTypeList = new ArrayList<>(List.of("invalidType", "special"));

        // Action
        criteriaExpression = new ContainsOnlyTypeExpression(expectedTypeList, "");

        // Assert messages
        String checkTypeListMessage = "Incorrect response given for a invalid list of criteria types";

        // Asserts
        Assertions.assertFalse(criteriaExpression.checkTypes(expectedTypeList), checkTypeListMessage);
    }

    @Test
    public void getTypeListTest() {
        // Expected values
        ArrayList<String> expectedTypeList = new ArrayList<>(List.of("letter", "special"));

        // Action
        criteriaExpression = new ContainsOnlyTypeExpression(expectedTypeList, "");

        // Assert messages
        String getTypeListMessage = "Incorrect typeList returned";

        // Asserts
        Assertions.assertEquals(expectedTypeList, criteriaExpression.getTypeList(), getTypeListMessage);
    }

    @Test
    public void getTypeListNullTest() {
        // Expected values (Not Required)

        // Action
        criteriaExpression = new SizeExpression(3, "123");

        // Assert messages
        String getTypeListMessage = "Unexpectedly returned a non-empty typeList";

        // Asserts
        Assertions.assertEquals(new ArrayList<>(), criteriaExpression.getTypeList(), getTypeListMessage);
    }

    @Test
    public void getConditionTest() {
        // Expected values (Not Required)

        // Action
        criteriaExpression = new SizeExpression(3, "123");

        // Assert messages
        String getConditionMessage = "Unexpectedly incorrect condition";

        // Asserts
        Assertions.assertEquals("size", criteriaExpression.getCondition(), getConditionMessage);
    }

    @Test
    public void getConditionNullTest() {
        // Expected values (Not Required)

        // Action
        criteriaExpression = new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter")), "123");

        // Assert messages
        String getConditionMessage = "Unexpectedly non-null condition";

        // Asserts
        Assertions.assertNull(criteriaExpression.getCondition(), getConditionMessage);
    }

    @Test
    public void getValueTest() {
        // Expected values
        int expectedValue = 3;

        // Action
        criteriaExpression = new SizeExpression(3, "123");

        // Assert messages
        String getValueMessage = "Unexpectedly incorrect value";

        // Asserts
        Assertions.assertEquals(String.valueOf(expectedValue), criteriaExpression.getValue(), getValueMessage);
    }

    @Test
    public void getValueNullTest() {
        // Expected values (Not Required)

        // Action
        criteriaExpression = new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter")), "123");

        // Assert messages
        String getValueMessage = "Unexpectedly non-null value";

        // Asserts
        Assertions.assertNull(criteriaExpression.getValue(), getValueMessage);
    }

    @Test
    public void getTargetTest() {
        // Expected values
        String expectedTarget = "123";

        // Action
        criteriaExpression = new SizeExpression(3, expectedTarget);

        // Assert messages
        String getTargetMessage = "Unexpectedly incorrect target";

        // Asserts
        Assertions.assertEquals(expectedTarget, criteriaExpression.getTarget(), getTargetMessage);
    }

    @Test
    public void getTargetTypeListTest() {
        // Expected values
        String expectedTarget = "123";

        // Action
        criteriaExpression = new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter")), expectedTarget);

        // Assert messages
        String getTargetMessage = "Unexpectedly incorrect target";

        // Asserts
        Assertions.assertEquals(expectedTarget, criteriaExpression.getTarget(), getTargetMessage);
    }

    @Test
    public void getLegendTest() {
        // Expected values
        HashMap<String, String> expectedLegend = new HashMap<>();

        expectedLegend.put("special", ",./;'[]=-`~!@#$%^&*()_+}{\":?><|");
        expectedLegend.put("number", "0123456789");
        expectedLegend.put("uppercase", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        expectedLegend.put("lowercase", expectedLegend.get("uppercase").toLowerCase());
        expectedLegend.put("letter", expectedLegend.get("uppercase") + expectedLegend.get("lowercase"));

        // Action
        criteriaExpression = new SizeExpression(3, "123");

        // Assert messages
        String getLegendMessage = "Unable to get correct legend";

        // Asserts
        Assertions.assertEquals(expectedLegend, criteriaExpression.getLegend(), getLegendMessage);
    }

    @Test
    public void setTargetTest() {
        // Expected values
        String expectedTarget = "123";

        // Action
        criteriaExpression = new SizeExpression(3, "234");
        criteriaExpression.setTarget(expectedTarget);

        // Assert messages
        String setTargetMessage = "Unable to set correct target";

        // Asserts
        Assertions.assertEquals(expectedTarget, criteriaExpression.getTarget(), setTargetMessage);
    }
}
