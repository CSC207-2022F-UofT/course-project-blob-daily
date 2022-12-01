package entities.criteria;

import com.backend.entities.criteria.Criteria;
import com.backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
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

public class CriteriaTest {
    private Criteria criteria;

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

        // Action
        criteria = new Criteria(new ArrayList<>(List.of(
                new SizeExpression(20, null),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special", "number", "letter", "uppercase", "lowercase")), null)
        )));

        // Assert messages
        String getConstructorMessage = "Unable to construct a criteria object";

        // Asserts
        Assertions.assertEquals(3, criteria.getExpressions().size(), getConstructorMessage);
        Assertions.assertEquals(SizeExpression.class, criteria.getExpressions().get(0).getClass(), getConstructorMessage);
        Assertions.assertEquals(ContainsOnlyTypeExpression.class, criteria.getExpressions().get(1).getClass(), getConstructorMessage);
        Assertions.assertEquals(ContainsAtleastTypeExpression.class, criteria.getExpressions().get(2).getClass(), getConstructorMessage);
    }

    @Test
    public void getExpressionsTest() {
        // Expected values
        ArrayList<CriteriaExpression> expectedExpressions = new ArrayList<>(List.of(
                new SizeExpression(20, null),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special", "number", "letter", "uppercase", "lowercase")), null)
        ));

        // Action
        criteria = new Criteria(expectedExpressions);

        // Assert messages
        String getExpressionsMessage = "Unable to get correct criteria expressions";

        // Asserts
        Assertions.assertEquals(3, criteria.getExpressions().size(), getExpressionsMessage);
        Assertions.assertEquals(expectedExpressions, criteria.getExpressions(), getExpressionsMessage);
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
        criteria = new Criteria(new ArrayList<>(List.of(
                new SizeExpression(20, null),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special", "number", "letter", "uppercase", "lowercase")), null)
        )));

        // Assert messages
        String getLegendMessage = "Unable to get correct legend";

        // Asserts
        Assertions.assertEquals(expectedLegend, criteria.getLegend(), getLegendMessage);
    }

    @Test
    public void toStringTest() {
        // Expected values
        String expectedStringRepresentation = "[size : 20 with the target of null, contains types [number, letter, special] with the target of null, contains types [special, number, letter, uppercase, lowercase] with the target of null]";

        // Action
        criteria = new Criteria(new ArrayList<>(List.of(
                new SizeExpression(20, null),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special", "number", "letter", "uppercase", "lowercase")), null)
        )));

        // Assert messages
        String toStringMessage = "Incorrect string representation";

        // Asserts
        Assertions.assertEquals(expectedStringRepresentation, criteria.toString(), toStringMessage);
    }
}
